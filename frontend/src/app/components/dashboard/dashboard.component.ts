import { ChangeDetectionStrategy, Component, DestroyRef, computed, inject, signal } from "@angular/core";
import { takeUntilDestroyed, toObservable } from "@angular/core/rxjs-interop";
import { combineLatest, switchMap, catchError, of } from "rxjs";
import { MultiLineChartComponent } from "../charts/multi-line-chart/multi-line-chart.component";
import { BarChartComponent } from "../charts/bar-chart/bar-chart.component";
import { ProgressGaugeComponent } from "../charts/progress-gauge-chart/progress-gauge-chart.component";
import { DonutChartComponent } from "../charts/donut-chart/donut-chart.component";
import { DashboardFilterComponent } from "./dashboard-filter/dashboard-filter.component";
import { Activity, AlertTriangle, CheckCircle2, Clock, ListTodo, LucideAngularModule } from "lucide-angular";
import { TaskTableComponent } from "./task-table/task-table.component";
import { BarSeriesData, DonutSlice, LineSeriesData } from "../../interfaces/charts.interface";
import { Task } from "../../interfaces/task.interface";
import {
    DashboardAnalyticsApiResponse,
    StatisticsCard,
    TaskDataDuration,
    TaskDataStatus,
    TaskStatus,
    TaskTableRow,
} from "../../interfaces/dashboard.interface";
import { DashboardAnalyticsService } from "../../services/dashboard-analytics.service";
import { TaskListStateService } from "../../services/task-list-state.service";
import { PlatformService } from "../../services/platform.service";
import { toDashboardTask } from "../../mappers/dashboard-analytics.mapper";

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    imports: [MultiLineChartComponent, BarChartComponent, ProgressGaugeComponent, DonutChartComponent, DashboardFilterComponent, LucideAngularModule, TaskTableComponent],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent {

    private readonly analyticsService     = inject(DashboardAnalyticsService);
    private readonly taskListStateService = inject(TaskListStateService);
    private readonly destroyRef           = inject(DestroyRef);
    readonly platformService              = inject(PlatformService);

    // ─── Filter state ──────────────────────────────────────────────────────────

    readonly filterDuration = signal<TaskDataDuration>('LAST_7_DAYS');
    readonly filterStatus   = signal<TaskDataStatus>('ALL');
    readonly filterListIds  = signal<number[]>([]);

    // ─── Raw fetched tasks ─────────────────────────────────────────────────────

    private readonly _tasks = signal<Task[]>([]);
    readonly loading        = signal(false);

    // ─── Date anchors (set once at init) ──────────────────────────────────────

    private readonly _todayStart = (() => {
        const d = new Date();
        d.setHours(0, 0, 0, 0);
        return d;
    })();
    private readonly _todayStr = this._todayStart.toDateString();

    // ─── Reactive fetch ────────────────────────────────────────────────────────

    constructor() {
        combineLatest([
            toObservable(this.filterDuration),
            toObservable(this.filterStatus),
            toObservable(this.filterListIds),
        ]).pipe(
            switchMap(([duration, status, listIds]) => {
                this.loading.set(true);
                return this.analyticsService.getAnalytics({
                    durationSelection: duration,
                    statusSelection:   status,
                    listSelections:    listIds,
                }).pipe(catchError(() => of({ tasks: [] } as DashboardAnalyticsApiResponse)));
            }),
            takeUntilDestroyed(this.destroyRef),
        ).subscribe(response => {
            this._tasks.set(response.tasks.map(toDashboardTask));
            this.loading.set(false);
        });
    }

    // ─── Statistics cards ──────────────────────────────────────────────────────

    readonly statisticsCards = computed<StatisticsCard[]>(() => {
        const tasks     = this._tasks();
        const total     = tasks.length;
        const completed = tasks.filter(t =>  t.completed).length;
        const active    = tasks.filter(t => !t.completed).length;
        const overdue   = tasks.filter(t => !t.completed && t.dueDate !== null && t.dueDate < this._todayStart).length;
        const dueToday  = tasks.filter(t => !t.completed && t.dueDate !== null && t.dueDate.toDateString() === this._todayStr).length;
        return [
            { label: 'Total',     value: total,     iconBackgroundColor: '#EFEFFD', icon: ListTodo     },
            { label: 'Completed', value: completed,  iconBackgroundColor: '#E8F9EE', icon: CheckCircle2 },
            { label: 'Active',    value: active,     iconBackgroundColor: '#EFEFFD', icon: Activity     },
            { label: 'Overdue',   value: overdue,    iconBackgroundColor: '#FDECEC', icon: AlertTriangle },
            { label: 'Due Today', value: dueToday,   iconBackgroundColor: '#FDF5E6', icon: Clock        },
        ];
    });

    // ─── Completion rate (gauge) ───────────────────────────────────────────────

    readonly completionRate = computed(() => {
        const tasks = this._tasks();
        if (tasks.length === 0) return 0;
        return Math.round(tasks.filter(t => t.completed).length / tasks.length * 100);
    });

    // ─── Tasks by list (donut) ─────────────────────────────────────────────────

    readonly tasksByList = computed<DonutSlice[]>(() => {
        const tasks = this._tasks();
        const lists = this.taskListStateService.lists();
        const counts = new Map<number | null, number>();
        for (const t of tasks) {
            counts.set(t.listId, (counts.get(t.listId) ?? 0) + 1);
        }
        return Array.from(counts.entries()).map(([listId, value]) => ({
            name:  lists.find(l => l.id === listId)?.name ?? 'No List',
            value,
        }));
    });

    // ─── Active vs Completed by list (bar) ────────────────────────────────────

    readonly activeVsCompletedByList = computed<{ categories: string[]; series: BarSeriesData[] }>(() => {
        const tasks       = this._tasks();
        const lists       = this.taskListStateService.lists();
        const categories  = lists.map(l => l.name);
        const activeData    = lists.map(l => tasks.filter(t => t.listId === l.id && !t.completed).length);
        const completedData = lists.map(l => tasks.filter(t => t.listId === l.id &&  t.completed).length);
        return {
            categories,
            series: [
                { name: 'Active',    data: activeData    },
                { name: 'Completed', data: completedData },
            ],
        };
    });

    // ─── Created vs Completed over time (line) ────────────────────────────────

    readonly createdVsCompletedOverTime = computed<{ categories: string[]; series: LineSeriesData[] }>(() =>
        buildTimeSeries(this._tasks(), this.filterDuration())
    );

    // ─── Task table rows ───────────────────────────────────────────────────────

    readonly taskTableRows = computed<TaskTableRow[]>(() => {
        const tasks = this._tasks();
        const lists = this.taskListStateService.lists();
        return tasks.map(t => {
            const list = lists.find(l => l.id === t.listId);
            const status: TaskStatus =
                t.completed                                          ? 'Completed' :
                t.dueDate !== null && t.dueDate < this._todayStart   ? 'Overdue'   : 'Active';
            return {
                title:     t.title,
                listName:  list?.name  ?? null,
                listColor: list?.color ?? null,
                dueDate:   t.dueDate,
                status,
            };
        });
    });
}

// ─── Time-series helper ────────────────────────────────────────────────────────

type Bucket = { label: string; start: Date; end: Date };

function buildTimeSeries(
    tasks: Task[],
    duration: TaskDataDuration,
): { categories: string[]; series: LineSeriesData[] } {
    const now     = new Date();
    const buckets: Bucket[] = [];

    if (duration === 'LAST_7_DAYS' || duration === 'LAST_30_DAYS') {
        const days = duration === 'LAST_7_DAYS' ? 7 : 30;
        for (let i = days - 1; i >= 0; i--) {
            const start = new Date(now);
            start.setDate(now.getDate() - i);
            start.setHours(0, 0, 0, 0);
            const end = new Date(start);
            end.setHours(23, 59, 59, 999);
            buckets.push({ label: start.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }), start, end });
        }
    } else if (duration === 'LAST_90_DAYS') {
        for (let i = 12; i >= 0; i--) {
            const end = new Date(now);
            end.setDate(now.getDate() - i * 7);
            end.setHours(23, 59, 59, 999);
            const start = new Date(end);
            start.setDate(end.getDate() - 6);
            start.setHours(0, 0, 0, 0);
            buckets.push({ label: start.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }), start, end });
        }
    } else {
        if (tasks.length === 0) return { categories: [], series: [] };
        const earliest = tasks.reduce((min, t) => (t.createdAt < min ? t.createdAt : min), tasks[0].createdAt);
        let cursor = new Date(earliest.getFullYear(), earliest.getMonth(), 1);
        while (cursor <= now) {
            const start = new Date(cursor);
            const end   = new Date(cursor.getFullYear(), cursor.getMonth() + 1, 0, 23, 59, 59, 999);
            buckets.push({ label: start.toLocaleDateString('en-US', { month: 'short', year: '2-digit' }), start, end });
            cursor = new Date(cursor.getFullYear(), cursor.getMonth() + 1, 1);
        }
    }

    const categories    = buckets.map(b => b.label);
    const createdData   = buckets.map(b => tasks.filter(t => t.createdAt >= b.start && t.createdAt <= b.end).length);
    const completedData = buckets.map(b =>
        tasks.filter(t => t.completedAt !== null && t.completedAt >= b.start && t.completedAt <= b.end).length
    );

    return {
        categories,
        series: [
            { name: 'Created',   data: createdData   },
            { name: 'Completed', data: completedData },
        ],
    };
}
