import { ChangeDetectionStrategy, Component, computed, inject, input, linkedSignal, output, signal } from '@angular/core';
import { LucideAngularModule, Circle, CheckCircle2, ChevronRight } from 'lucide-angular';
import { Task, TaskList } from '../../interfaces/task.interface';
import { TaskStateService } from '../../services/task-state.service';
import { DatepickerComponent } from '../../shared/components/datepicker/datepicker.component';
import { toLocalDateString, formatDisplayDate } from '../../utils/date.utils';
import { CdkDragDrop, DragDropModule, moveItemInArray } from '@angular/cdk/drag-drop';
import { TaskListViewSortOptions } from '../../interfaces/task-list-view';

@Component({
    selector: 'app-task-list-view',
    templateUrl: './task-list-view.component.html',
    styleUrls: ['./task-list-view.component.scss'],
    imports: [LucideAngularModule, DatepickerComponent, DragDropModule],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskListViewComponent {
    readonly circleIcon = Circle;
    readonly checkCircleIcon = CheckCircle2;
    readonly chevronRightIcon = ChevronRight;

    private readonly taskService = inject(TaskStateService);

    componentTitle = input.required<string>();
    tasks = input.required<Task[]>();
    lists = input.required<TaskList[]>();
    searchQuery = input<string>('');
    defaultListId = input<number | null>(null);
    defaultDueDate = input<Date | null>(null);
    dragAndDropEnabled = input<boolean>(false);
    sortOption = input<TaskListViewSortOptions>(TaskListViewSortOptions.DATE);

    taskSelected = output<Task>();

    newTaskTitle = signal<string>('');
    newTaskDueDate = signal<Date | null>(null);

    readonly filteredTasks = computed(() => {
        const query = this.searchQuery().trim().toLowerCase();
        if (!query) return this.tasks();
        return this.tasks().filter(t => t.title.toLowerCase().includes(query));
    });

    readonly sortedTasks = linkedSignal({
        source: () => this.filteredTasks(),
        computation: () => {
            const sortOption = this.sortOption();
            if (sortOption === TaskListViewSortOptions.DATE) {
                // When putting in this sort option, it's assumed that each entry has a due date. Will cause problems if that
                //  assumption is wrong :) 
                return this.filteredTasks().sort((a, b) => new Date(a.dueDate ?? new Date()).getTime() - new Date(b.dueDate ?? new Date()).getTime());
            } else if (sortOption === TaskListViewSortOptions.POSITION) {
                return this.filteredTasks().sort((a, b) => a.position - b.position)
            } else {
                return this.filteredTasks().sort((a, b) => a.title.localeCompare(b.title));
            }
        }
    });

    readonly incompleteCount = computed(() =>
        this.filteredTasks().filter(t => !t.completed).length
    );

    getListForTask(listId: number | null): TaskList | undefined | null {
        if (listId === null) return null;
        return this.lists().find(l => l.id === listId);
    }

    getSubtaskProgress(task: Task): string | null {
        if (task.subtasks.length === 0) return null;
        const done = task.subtasks.filter(s => s.completed).length;
        return `${done}/${task.subtasks.length}`;
    }

    formatDate(date: Date): string {
        return formatDisplayDate(date);
    }

    toggleTask(taskId: number): void {
        const task = this.taskService.tasks().find(t => t.id === taskId);
        if (!task) return;
        this.taskService.updateTask(taskId, {
            title: task.title,
            notes: task.notes,
            dueDate: task.dueDate ? toLocalDateString(task.dueDate) : null,
            listId: task.listId,
            completed: !task.completed,
            position: task.position
        });
    }

    openDetails(task: Task): void {
        this.taskSelected.emit(task);
    }

    addTask(): void {
        const title = this.newTaskTitle().trim();
        if (!title) return;
        const dueDate = this.newTaskDueDate() ?? this.defaultDueDate();
        this.taskService.addTask({
            title,
            dueDate: dueDate ? toLocalDateString(dueDate) : null,
            listId: this.defaultListId(),
        });
        this.newTaskTitle.set('');
        this.newTaskDueDate.set(null);
    }

    onAddTaskKeydown(event: KeyboardEvent): void {
        if (event.key === 'Enter') {
            event.preventDefault();
            this.addTask();
        }
    }

    drop(event: CdkDragDrop<Task[]>) {
        if (event.previousIndex === event.currentIndex) {
            return;
        }

        const tasks = this.sortedTasks();
        const task = tasks[event.previousIndex];

        moveItemInArray(tasks, event.previousIndex, event.currentIndex);

        this.taskService.moveTask(task.id, {
            title: task.title,
            notes: task.notes,
            dueDate: task.dueDate ? toLocalDateString(task.dueDate) : null,
            listId: task.listId,
            completed: task.completed,
            position: event.currentIndex
        });
    }
}
