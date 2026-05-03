import { ChangeDetectionStrategy, Component, computed, inject, input, output } from '@angular/core';
import { Task } from '../../interfaces/task.interface';
import { TaskStateService } from '../../services/task-state.service';
import { TaskListStateService } from '../../services/task-list-state.service';
import { TaskListViewComponent } from '../task-list-view/task-list-view.component';

@Component({
    selector: 'app-list-view',
    templateUrl: './list-view.component.html',
    imports: [TaskListViewComponent],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ListViewComponent {
    private readonly taskService = inject(TaskStateService);
    private readonly taskListService = inject(TaskListStateService);

    listId = input.required<number>();
    searchQuery = input<string>('');
    taskSelected = output<Task>();

    readonly lists = this.taskListService.lists;

    readonly list = computed(() =>
        this.taskListService.lists().find(l => l.id === this.listId())
    );

    readonly listTasks = computed(() =>
        this.taskService.tasks().filter(t => t.listId === this.listId())
    );
}
