import { ChangeDetectionStrategy, Component, computed, inject, signal, viewChild } from "@angular/core";
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { SidebarComponent } from "../../shared/components/sidebar/sidebar.component";
import { NavItem } from "../../shared/interfaces/sidebar.interface";
import { Sun, Calendar, LucideAngularModule, Plus, Ellipsis, Clock, ChartColumn } from "lucide-angular";
import { Task, TaskList, TaskListTemplate, TaskListTemplateColor } from "../../interfaces/task.interface";
import { TaskListStateService } from '../../services/task-list-state.service';
import { LandingPageHeaderComponent } from "../../components/headers/landing-page-header/landing-page-header.component";
import { TaskDetailsPanelComponent } from "../../components/task-details-panel/task-details-panel.component";
import { MyDayComponent } from "../../components/my-day/my-day.component";
import { ListViewComponent } from "../../components/list-view/list-view.component";
import { CalendarViewComponent } from "../../components/calendar-view/calendar-view.component";
import { AutoFocusDirective } from "../../shared/directives/auto-focus.directive";
import { NewTemplateModalComponent } from "../../components/new-template-modal/new-template-modal.component";
import { TaskListTemplateStateService } from "../../services/task-list-template-state.service";
import { R } from "@angular/cdk/keycodes";
import { CdkScrollable } from "@angular/cdk/scrolling";
import { TaskStateService } from "../../services/task-state.service";
import { UpcomingComponent } from "../../components/upcoming/upcoming.component";
import { DashboardComponent } from "../../components/dashboard/dashboard.component";

export type AppView = 'my-day' | 'upcoming' | 'calendar' | 'dashboard' | 'list';

@Component({
    selector: 'app-landing-page',
    templateUrl: './landing-page.component.html',
    styleUrls: ['./landing-page.component.scss'],
    imports: [
        SidebarComponent, 
        LucideAngularModule, 
        LandingPageHeaderComponent, 
        TaskDetailsPanelComponent, 
        MyDayComponent, 
        ListViewComponent, 
        CalendarViewComponent, 
        AutoFocusDirective, 
        NewTemplateModalComponent, 
        UpcomingComponent, 
        DashboardComponent,
        CdkScrollable
    ],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LandingPageComponent {

    private readonly taskService = inject(TaskStateService);
    private readonly taskListService = inject(TaskListStateService);
    private readonly taskListTemplateStateService = inject(TaskListTemplateStateService);
    private readonly route = inject(ActivatedRoute);
    private readonly router = inject(Router);
    private readonly queryParamMap = toSignal(this.route.queryParamMap);
    

    readonly tasks = this.taskService.tasks;
    readonly lists = this.taskListService.lists;
    readonly templates = this.taskListTemplateStateService.templates;

    currentView = computed<AppView>(() => {
        const view = this.queryParamMap()?.get('view');
        return (view === 'my-day' || view === 'upcoming' || view === 'calendar' || view === 'dashboard' || view === 'list')
            ? (view as AppView)
            : 'my-day';
    });

    selectedListId = computed<number | null>(() => {
        const raw = this.queryParamMap()?.get('listId');
        const id = raw ? parseInt(raw, 10) : NaN;
        return this.currentView() === 'list' && !isNaN(id) ? id : null;
    });
    searchQuery = signal<string>('');
    selectedTask = signal<Task | null>(null);
    addingList = signal<boolean>(false);
    addingTemplate = signal<boolean>(false);
    selectedTemplate = signal<TaskListTemplate | null>(null);

    editingListId = signal<number | null>(null);
    editingListName = signal<string>('');
    editingListColor = signal<string>(TaskListTemplateColor.BLUE);
    newListColor = signal<string>(TaskListTemplateColor.BLUE);

    readonly listColors = Object.values(TaskListTemplateColor);

    private _editCancelled = false;

    navItems = computed<NavItem[]>(() => [
        {
            navItemLabel: 'My Day',
            navItemIcon: Sun,
            navItemRouteFn: () => this.navigateTo('my-day')
        },
        {
            navItemLabel: 'Upcoming',
            navItemIcon: Clock,
            navItemRouteFn: () => this.navigateTo('upcoming')
        },
        {
            navItemLabel: 'Calendar',
            navItemIcon: Calendar,
            navItemRouteFn: () => this.navigateTo('calendar')
        },
        {
            navItemLabel: 'Dashboard',
            navItemIcon: ChartColumn,
            navItemRouteFn: () => this.navigateTo('dashboard')
        }
    ]);

    readonly sortedLists = computed(() => 
        this.lists().sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())    
    );

    readonly sidebarComponent = viewChild.required<SidebarComponent>('sidebar');

    readonly plus = Plus;
    readonly ellipsis = Ellipsis;

    selectListView(listId: number): void {
        this.router.navigate([], {
            relativeTo: this.route,
            queryParams: { view: 'list', listId },
            queryParamsHandling: 'merge'
        }).catch(console.error);
        this.sidebarComponent().isExpanded.set(false);
    }

    private navigateTo(view: AppView): void {
        this.router.navigate([], {
            relativeTo: this.route,
            queryParams: { view, listId: null },
            queryParamsHandling: 'merge'
        }).catch(console.error);
    }

    startAddingList(): void {
        this.newListColor.set(TaskListTemplateColor.BLUE);
        this.addingList.set(true);
    }

    confirmAddList(name: string): void {
        const trimmed = name.trim();
        if (trimmed) {
            this.taskListService.addList({ name: trimmed, color: this.newListColor() });
        }
        this.addingList.set(false);
    }

    cancelAddList(): void {
        this.addingList.set(false);
    }

    startEditingList(event: Event, list: TaskList): void {
        event.stopPropagation();
        this._editCancelled = false;
        this.editingListId.set(list.id);
        this.editingListName.set(list.name);
        this.editingListColor.set(list.color);
    }

    confirmEditList(): void {
        if (this._editCancelled) {
            this._editCancelled = false;
            return;
        }
        const id = this.editingListId();
        const name = this.editingListName().trim();
        if (id !== null && name) {
            this.taskListService.updateList(id, { name, color: this.editingListColor() });
        }
        this.editingListId.set(null);
    }

    cancelEditList(): void {
        this._editCancelled = true;
        this.editingListId.set(null);
    }
    
    startAddingTemplate(): void {
        this.addingTemplate.set(true);
    }

    stopAddingTemplate(): void {
        this.addingTemplate.set(false);
        this.selectedTemplate.set(null);
    }

    editTemplate(template: TaskListTemplate): void {
        this.selectedTemplate.set(template);
        this.addingTemplate.set(true);
    }

    fetchTaskById(id: number) {
        return this.tasks().filter(t => t.id === id).pop() ?? null;
    }
}