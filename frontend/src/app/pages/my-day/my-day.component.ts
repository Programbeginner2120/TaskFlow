import { Component, computed, inject, input, output, signal } from '@angular/core';
import { LucideAngularModule, Circle, CheckCircle2, ChevronRight } from 'lucide-angular';
import { Task } from '../../shared/interfaces/task.interface';
import { TaskStateService } from '../../services/task-state.service';
import { TaskListStateService } from '../../services/task-list-state.service';
import { DatepickerComponent } from '../../shared/components/datepicker/datepicker.component';
import { toLocalDateString } from '../../utils/date.utils';

@Component({
    selector: 'app-my-day',
    templateUrl: './my-day.component.html',
    styleUrls: ['./my-day.component.scss'],
    imports: [LucideAngularModule, DatepickerComponent],
})
export class MyDayComponent {
    readonly circleIcon = Circle;
    readonly checkCircleIcon = CheckCircle2;
    readonly chevronRightIcon = ChevronRight;

    private readonly taskService = inject(TaskStateService);
    private readonly taskListService = inject(TaskListStateService);

    searchQuery = input<string>('');
    taskSelected = output<Task>();

    newTaskTitle = signal<string>('');
    newTaskDueDate = signal<Date | null>(null);

    private readonly today = new Date();

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

    readonly filteredTasks = computed(() => {
        const query = this.searchQuery().trim().toLowerCase();
        if (!query) return this.todaysTasks();
        return this.todaysTasks().filter(t => t.title.toLowerCase().includes(query));
    });

    readonly sortedTasks = computed(() => {
        const tasks = this.filteredTasks();
        return [
            ...tasks.filter(t => !t.completed),
            ...tasks.filter(t => t.completed),
        ];
    });

    readonly incompleteCount = computed(() =>
        this.filteredTasks().filter(t => !t.completed).length
    );

    getListForTask(listId: number) {
        return this.taskListService.lists().find(l => l.id === listId);
    }

    getSubtaskProgress(task: Task): string | null {
        if (task.subtasks.length === 0) return null;
        const done = task.subtasks.filter(s => s.completed).length;
        return `${done}/${task.subtasks.length}`;
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
        });
    }

    openDetails(task: Task): void {
        this.taskSelected.emit(task);
    }

    addTask(): void {
        const title = this.newTaskTitle().trim();
        if (!title) return;
        const dueDate = this.newTaskDueDate() ?? new Date();
        this.taskService.addTask({ title, dueDate: toLocalDateString(dueDate) });
        this.newTaskTitle.set('');
        this.newTaskDueDate.set(null);
    }

    onAddTaskKeydown(event: KeyboardEvent): void {
        if (event.key === 'Enter') {
            event.preventDefault();
            this.addTask();
        }
    }
}
