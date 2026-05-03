import { ChangeDetectionStrategy, Component, computed, inject, input, output } from '@angular/core';
import { Task } from '../../interfaces/task.interface';
import { TaskStateService } from '../../services/task-state.service';
import { TaskListStateService } from '../../services/task-list-state.service';
import { TaskListViewComponent } from '../task-list-view/task-list-view.component';

@Component({
    selector: 'app-my-day',
    templateUrl: './my-day.component.html',
    styleUrls: ['./my-day.component.scss'],
    imports: [TaskListViewComponent],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MyDayComponent {
    private readonly taskService = inject(TaskStateService);
    private readonly taskListService = inject(TaskListStateService);

    searchQuery = input<string>('');
    taskSelected = output<Task>();

    readonly today = new Date();
    readonly lists = this.taskListService.lists;

    private isSameDay(a: Date, b: Date): boolean {
        return (
            a.getFullYear() === b.getFullYear() &&
            a.getMonth() === b.getMonth() &&
            a.getDate() === b.getDate()
        );
    }

    readonly todaysTasks = computed(() =>
        this.taskService.tasks().filter(
            t => t.dueDate !== null && this.isSameDay(t.dueDate, this.today)
        )
    );

}
