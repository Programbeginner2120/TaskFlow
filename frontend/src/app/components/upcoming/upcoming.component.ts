import { ChangeDetectionStrategy, Component, computed, inject, input, output } from '@angular/core';
import { Task } from '../../interfaces/task.interface';
import { TaskStateService } from '../../services/task-state.service';
import { TaskListStateService } from '../../services/task-list-state.service';
import { TaskListViewComponent } from '../task-list-view/task-list-view.component';

@Component({
    selector: 'app-upcoming',
    templateUrl: './upcoming.component.html',
    styleUrls: ['./upcoming.component.scss'],
    imports: [TaskListViewComponent],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UpcomingComponent {
    private readonly taskService = inject(TaskStateService);
    private readonly taskListService = inject(TaskListStateService);

    searchQuery = input<string>('');
    taskSelected = output<Task>();

    readonly today = new Date();
    readonly lists = this.taskListService.lists;

    readonly thisWeeksTasks = computed(() =>
        this.taskService.tasks().filter(
            t => t.dueDate !== null && t.dueDate >= this.today && t.dueDate.getTime() <= this.today.getTime() + (1000 * 24 * 60 * 60 * 7) // represents one week out
        )
    );

}
