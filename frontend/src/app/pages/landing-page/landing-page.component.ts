import { ChangeDetectionStrategy, Component, computed, inject, signal, viewChild } from "@angular/core";
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { SidebarComponent } from "../../shared/components/sidebar/sidebar.component";
import { NavItem } from "../../shared/interfaces/sidebar.interface";
import { Sun, Calendar, LucideAngularModule, Plus, Ellipsis, Clock } from "lucide-angular";
import { Task, TaskListTemplate } from "../../interfaces/task.interface";
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
import { TaskStateService } from "../../services/task-state.service";
import { UpcomingComponent } from "../../components/upcoming/upcoming.component";

export type AppView = 'my-day' | 'upcoming' | 'calendar' | 'list';

@Component({
    selector: 'app-landing-page',
    templateUrl: './landing-page.component.html',
    styleUrls: ['./landing-page.component.scss'],
    imports: [SidebarComponent, LucideAngularModule, LandingPageHeaderComponent, TaskDetailsPanelComponent, MyDayComponent, ListViewComponent, CalendarViewComponent, AutoFocusDirective, NewTemplateModalComponent, UpcomingComponent],
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
        return (view === 'my-day' || view === 'upcoming' || view === 'calendar' || view === 'list')
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
        }
    ]);

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
        this.addingList.set(true);
    }

    confirmAddList(name: string): void {
        const trimmed = name.trim();
        if (trimmed) {
            this.taskListService.addList({ name: trimmed });
        }
        this.addingList.set(false);
    }

    cancelAddList(): void {
        this.addingList.set(false);
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