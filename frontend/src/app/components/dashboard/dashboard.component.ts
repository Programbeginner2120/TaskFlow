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
            toObservable(this.filterListIds),
        ]).pipe(
            switchMap(([duration, listIds]) => {
                this.loading.set(true);
                return this.analyticsService.getAnalytics({
                    durationSelection: duration,
                    statusSelection:   'ALL',
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

    // ─── Task age breakdown (donut) ──────────────────────────────────────────

    readonly taskAgeBreakdown = computed<DonutSlice[]>(() => {
        const activeTasks = this._tasks().filter(t => !t.completed);
        const todayMs     = this._todayStart.getTime();
        const msPerDay    = 1000 * 60 * 60 * 24;
        const ageDays     = (t: Task) => {
            const d = new Date(t.createdAt);
            d.setHours(0, 0, 0, 0);
            return (todayMs - d.getTime()) / msPerDay;
        };

        if (this.filterDuration() === 'LAST_7_DAYS') {
            const today     = activeTasks.filter(t => ageDays(t) === 0).length;
            const yesterday = activeTasks.filter(t => ageDays(t) === 1).length;
            const thisWeek  = activeTasks.filter(t => { const d = ageDays(t); return d >= 2 && d <= 6; }).length;
            return [
                { name: 'Today',               value: today     },
                { name: 'Yesterday',            value: yesterday },
                { name: 'This Week (2–6 days)', value: thisWeek  },
            ];
        }

        const fresh  = activeTasks.filter(t => ageDays(t) === 0).length;
        const recent = activeTasks.filter(t => { const d = ageDays(t); return d >= 1  && d <= 7;  }).length;
        const aging  = activeTasks.filter(t => { const d = ageDays(t); return d >= 8  && d <= 30; }).length;
        const stale  = activeTasks.filter(t => ageDays(t) > 30).length;

        return [
            { name: 'Fresh (today)',     value: fresh  },
            { name: 'Recent (1–7 days)', value: recent },
            { name: 'Aging (8–30 days)', value: aging  },
            { name: 'Stale (30+ days)',  value: stale  },
        ];
    });

    // ─── Productivity by day of week (bar) ──────────────────────────────────────

    readonly productivityByDayOfWeek = computed<{ categories: string[]; series: BarSeriesData[] }>(() => {
        const createdCounts   = [0, 0, 0, 0, 0, 0, 0]; // Mon–Sun
        const completedCounts = [0, 0, 0, 0, 0, 0, 0];
        for (const t of this._tasks()) {
            const createdDay = (t.createdAt.getDay() + 6) % 7; // JS Sun=0 → Mon=0
            createdCounts[createdDay]++;
            if (t.completedAt !== null) {
                const completedDay = (t.completedAt.getDay() + 6) % 7;
                completedCounts[completedDay]++;
            }
        }
        return {
            categories: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            series: [
                { name: 'Created',   data: createdCounts   },
                { name: 'Completed', data: completedCounts },
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

    if (duration === 'LAST_7_DAYS') {
        // One bucket per day for the past 7 days (i=6 is oldest, i=0 is today)
        for (let i = 6; i >= 0; i--) {
            const start = new Date(now);
            start.setDate(now.getDate() - i);
            start.setHours(0, 0, 0, 0);
            const end = new Date(start);
            end.setHours(23, 59, 59, 999);
            buckets.push({ label: start.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }), start, end });
        }
    } else if (duration === 'LAST_30_DAYS') {
        // Weekly (7-day) buckets from oldest to newest; the newest bucket may be shorter
        // if 30 is not divisible by 7 (it's not: 30 % 7 = 2 — last bucket covers 2 days)
        for (let daysFromNow = 29; daysFromNow >= 0; daysFromNow -= 7) {
            const start = new Date(now);
            start.setDate(now.getDate() - daysFromNow);
            start.setHours(0, 0, 0, 0);
            const end = new Date(now);
            end.setDate(now.getDate() - Math.max(0, daysFromNow - 6));
            end.setHours(23, 59, 59, 999);
            buckets.push({ label: start.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }), start, end });
        }
    } else if (duration === 'LAST_90_DAYS') {
        // 3-week (21-day) buckets from oldest to newest; the newest bucket may be shorter
        // if 90 is not divisible by 21 (it's not: 90 % 21 = 6 — last bucket covers 6 days)
        for (let daysFromNow = 89; daysFromNow >= 0; daysFromNow -= 21) {
            const start = new Date(now);
            start.setDate(now.getDate() - daysFromNow);
            start.setHours(0, 0, 0, 0);
            const end = new Date(now);
            end.setDate(now.getDate() - Math.max(0, daysFromNow - 20));
            end.setHours(23, 59, 59, 999);
            buckets.push({ label: start.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }), start, end });
        }
    } else {
        // ALL_TIME: one bucket per calendar month from the earliest task's month up to now
        if (tasks.length === 0) return { categories: [], series: [] };
        const earliest = tasks.reduce((min, t) => (t.createdAt < min ? t.createdAt : min), tasks[0].createdAt);
        let cursor = new Date(earliest.getFullYear(), earliest.getMonth(), 1);
        while (cursor <= now) {
            const start = new Date(cursor);
            // Last day of the month at 23:59:59 — day 0 of next month rolls back to last day of current month
            const end   = new Date(cursor.getFullYear(), cursor.getMonth() + 1, 0, 23, 59, 59, 999);
            buckets.push({ label: start.toLocaleDateString('en-US', { month: 'short', year: '2-digit' }), start, end });
            cursor = new Date(cursor.getFullYear(), cursor.getMonth() + 1, 1);
        }
    }

    // Map each bucket to a count of tasks whose createdAt / completedAt falls within [start, end]
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
