import { Component, computed, inject, signal, viewChild } from "@angular/core";
import { SidebarComponent } from "../../shared/components/sidebar/sidebar.component";
import { NavItem } from "../../shared/interfaces/sidebar.interface";
import { Calendar, Clock, Sun, LucideAngularModule, Plus, Ellipsis } from "lucide-angular";
import { Task } from "../../shared/interfaces/task.interface";
import { TaskListStateService } from '../../services/task-list-state.service';
import { LandingPageHeaderComponent } from "../../components/headers/landing-page-header/landing-page-header.component";
import { TaskDetailsPanelComponent } from "../../shared/components/task-details-panel/task-details-panel.component";
import { MyDayComponent } from "../../components/my-day/my-day.component";
import { ListViewComponent } from "../../components/list-view/list-view.component";
import { AutoFocusDirective } from "../../shared/directives/auto-focus.directive";

export type AppView = 'my-day' | 'upcoming' | 'calendar' | 'list';

@Component({
    selector: 'app-landing-page',
    templateUrl: './landing-page.component.html',
    styleUrls: ['./landing-page.component.scss'],
    imports: [SidebarComponent, LucideAngularModule, LandingPageHeaderComponent, TaskDetailsPanelComponent, MyDayComponent, ListViewComponent, AutoFocusDirective]
})
export class LandingPageComponent {

    private readonly taskListService = inject(TaskListStateService);

    readonly lists = this.taskListService.lists;

    currentView = signal<AppView>('my-day');
    selectedListId = signal<number | null>(null);
    searchQuery = signal<string>('');
    selectedTask = signal<Task | null>(null);
    addingList = signal(false);

    navItems = computed<NavItem[]>(() => [
        {
            navItemLabel: 'My Day',
            navItemIcon: Sun,
            navItemRouteFn: () => this.currentView.set('my-day')
        },
        // {
        //     navItemLabel: 'Upcoming',
        //     navItemIcon: Clock,
        //     navItemRouteFn: () => this.currentView.set('upcoming')
        // },
        // {
        //     navItemLabel: 'Calendar',
        //     navItemIcon: Calendar,
        //     navItemRouteFn: () => this.currentView.set('calendar')
        // }
    ]);

    readonly sidebarComponent = viewChild.required<SidebarComponent>('sidebar');

    readonly plus = Plus;
    readonly ellipsis = Ellipsis;

    selectListView(listId: number): void {
        this.selectedListId.set(listId);
        this.currentView.set('list');
        this.sidebarComponent().isExpanded.set(false);
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
}