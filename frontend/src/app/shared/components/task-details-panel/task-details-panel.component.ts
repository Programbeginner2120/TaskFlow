import {
    Component,
    computed,
    effect,
    ElementRef,
    inject,
    input,
    OnDestroy,
    output,
    signal,
    viewChildren,
} from '@angular/core';
import { LucideAngularModule, X, Calendar, Check } from 'lucide-angular';
import { Task } from '../../interfaces/task.interface';
import { TaskStateService } from '../../../services/task-state.service';
import { TaskListStateService } from '../../../services/task-list-state.service';
import { SelectComponent } from '../select/select.component';
import { DatepickerComponent } from '../datepicker/datepicker.component';
import { SelectOption } from '../../interfaces/select.interface';
import { PlatformService } from '../../../services/platform.service';
import { toLocalDateString } from '../../../utils/date.utils';

@Component({
    selector: 'app-task-details-panel',
    templateUrl: './task-details-panel.component.html',
    styleUrls: ['./task-details-panel.component.scss'],
    imports: [LucideAngularModule, SelectComponent, DatepickerComponent],
})
export class TaskDetailsPanelComponent implements OnDestroy {
    readonly closeIcon = X;
    readonly calendarIcon = Calendar;
    readonly checkIcon = Check;

    private readonly taskService = inject(TaskStateService);
    private readonly taskListService = inject(TaskListStateService);
    readonly platformService = inject(PlatformService);

    task = input.required<Task | null>();
    close = output<void>();

    newSubtaskTitle = signal<string>('');
    dueDate = signal<Date | null>(null);

    readonly listOptions = computed<SelectOption[]>(() =>
        this.taskListService.lists().map(l => ({ value: l.id, label: l.name }))
    );

    readonly subtaskProgress = computed(() => {
        const t = this.task();
        if (!t || t.subtasks.length === 0) return null;
        const done = t.subtasks.filter(s => s.completed).length;
        return `${done}/${t.subtasks.length}`;
    });

    readonly selectedListId = computed<number | null>(() => {
        const t = this.task();
        return t ? t.listId : null;
    });

    readonly formattedDueDate = computed(() => {
        const date = this.dueDate();
        if (!date) return 'No due date';
        return date.toLocaleDateString('en-US', {
            month: 'long',
            day: 'numeric',
            year: 'numeric',
        });
    });

    private keydownHandler = (event: KeyboardEvent) => {
        if (event.key === 'Escape') {
            this.close.emit();
        }
    };

    constructor() {
        effect(() => {
            const t = this.task();
            if (t) {
                document.addEventListener('keydown', this.keydownHandler);
                this.dueDate.set(t.dueDate);
            } else {
                document.removeEventListener('keydown', this.keydownHandler);
            }
        });

        effect(() => {
            const t = this.task();
            if (!t) return;
            const date = this.dueDate();
            // Only write back when value differs from the current task state
            const current = t.dueDate;
            const changed =
                (date === null && current !== null) ||
                (date !== null && (current === null || date.getTime() !== current.getTime()));
            if (changed) {
                this.taskService.updateTask(t.id, {
                    title: t.title,
                    notes: t.notes,
                    dueDate: date ? toLocalDateString(date) : null,
                    listId: t.listId,
                    completed: t.completed,
                });
            }
        });
    }

    ngOnDestroy(): void {
        document.removeEventListener('keydown', this.keydownHandler);
    }

    onBackdropClick(event: MouseEvent): void {
        if (event.target === event.currentTarget) {
            this.close.emit();
        }
    }

    onTitleChange(event: Event): void {
        const t = this.task();
        if (!t) return;
        const value = (event.target as HTMLInputElement).value.trim();
        if (!value) return;
        this.taskService.updateTask(t.id, {
            title: value,
            notes: t.notes,
            dueDate: t.dueDate ? toLocalDateString(t.dueDate) : null,
            listId: t.listId,
            completed: t.completed,
        });
    }

    onNotesChange(event: Event): void {
        const t = this.task();
        if (!t) return;
        const value = (event.target as HTMLTextAreaElement).value;
        this.taskService.updateTask(t.id, {
            title: t.title,
            notes: value,
            dueDate: t.dueDate ? toLocalDateString(t.dueDate) : null,
            listId: t.listId,
            completed: t.completed,
        });
    }

    onListChange(value: string | number | null): void {
        const t = this.task();
        if (!t || value === null) return;
        this.taskService.updateTask(t.id, {
            title: t.title,
            notes: t.notes,
            dueDate: t.dueDate ? toLocalDateString(t.dueDate) : null,
            listId: value as number,
            completed: t.completed,
        });
    }

    toggleSubtask(subtaskId: number): void {
        const t = this.task();
        if (!t) return;
        const subtask = t.subtasks.find(s => s.id === subtaskId);
        if (!subtask) return;
        this.taskService.updateSubtask(t.id, subtaskId, {
            title: subtask.title,
            completed: !subtask.completed,
        });
    }

    addSubtask(): void {
        const t = this.task();
        const title = this.newSubtaskTitle().trim();
        if (!t || !title) return;
        this.newSubtaskTitle.set('');
        this.taskService.addSubtask(t.id, { title });
    }

    onSubtaskKeydown(event: KeyboardEvent): void {
        if (event.key === 'Enter') {
            event.preventDefault();
            this.addSubtask();
        }
    }
}
