import {
    ChangeDetectionStrategy,
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
import { TaskStateService } from '../../services/task-state.service';
import { TaskListStateService } from '../../services/task-list-state.service';
import { SelectComponent } from '../../shared/components/select/select.component';
import { DatepickerComponent } from '../../shared/components/datepicker/datepicker.component';
import { SelectOption } from '../../shared/interfaces/select.interface';
import { PlatformService } from '../../services/platform.service';
import { toLocalDateString } from '../../utils/date.utils';

@Component({
    selector: 'app-task-details-panel',
    templateUrl: './task-details-panel.component.html',
    styleUrls: ['./task-details-panel.component.scss'],
    imports: [LucideAngularModule, SelectComponent, DatepickerComponent],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskDetailsPanelComponent implements OnDestroy {
    readonly closeIcon = X;
    readonly calendarIcon = Calendar;
    readonly checkIcon = Check;

    private readonly taskService = inject(TaskStateService);
    private readonly taskListService = inject(TaskListStateService);
    readonly platformService = inject(PlatformService);

    task = input.required<Task | null>();

    triggerTaskReload = output<number>();
    close = output<void>();

    newSubtaskTitle = signal<string>('');
    dueDate = signal<Date | null>(null);

    // Local draft state — buffered until the panel closes
    private readonly localTitle = signal<string>('');
    private readonly localNotes = signal<string | null>(null);
    private readonly localListId = signal<number | null>(null);

    readonly listOptions = computed<SelectOption[]>(() =>
        this.taskListService.lists().map(l => ({ value: l.id, label: l.name }))
    );

    readonly subtaskProgress = computed(() => {
        const t = this.task();
        if (!t || t.subtasks.length === 0) return null;
        const done = t.subtasks.filter(s => s.completed).length;
        return `${done}/${t.subtasks.length}`;
    });

    readonly selectedListId = computed<number | null>(() => this.localListId());

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
            this.handleClose();
        }
    };

    constructor() {
        effect(() => {
            const t = this.task();
            if (t) {
                document.addEventListener('keydown', this.keydownHandler);
                this.localTitle.set(t.title);
                this.localNotes.set(t.notes ?? null);
                this.localListId.set(t.listId);
                this.dueDate.set(t.dueDate);
            } else {
                document.removeEventListener('keydown', this.keydownHandler);
            }
        });
    }

    ngOnDestroy(): void {
        document.removeEventListener('keydown', this.keydownHandler);
    }

    handleClose(): void {
        // Blur the active element so any focused input's (blur) fires before we read local state
        (document.activeElement as HTMLElement)?.blur();
        this.saveIfChanged();
        this.close.emit();
    }

    private saveIfChanged(): void {
        const t = this.task();
        if (!t) return;

        const title = this.localTitle();
        const notes = this.localNotes();
        const listId = this.localListId();
        const dueDate = this.dueDate();

        const dueDateChanged =
            (dueDate === null && t.dueDate !== null) ||
            (dueDate !== null && (t.dueDate === null || dueDate.getTime() !== t.dueDate.getTime()));

        if (title !== t.title || notes !== t.notes || listId !== t.listId || dueDateChanged) {
            this.taskService.updateTask(t.id, {
                title,
                notes,
                dueDate: dueDate ? toLocalDateString(dueDate) : null,
                listId,
                completed: t.completed
            });
        }
    }

    onBackdropClick(event: MouseEvent): void {
        if (event.target === event.currentTarget) {
            this.handleClose();
        }
    }

    onTitleChange(event: Event): void {
        const value = (event.target as HTMLInputElement).value.trim();
        if (value) this.localTitle.set(value);
    }

    onNotesChange(event: Event): void {
        this.localNotes.set((event.target as HTMLTextAreaElement).value);
    }

    onListChange(value: string | number | null): void {
        this.localListId.set(value as number | null);
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
        this.taskService.addSubtask(t.id, { title }).subscribe({
                next: () => this.triggerTaskReload.emit(t.id),
                error: console.error
        });
    }

    onSubtaskKeydown(event: KeyboardEvent): void {
        if (event.key === 'Enter') {
            event.preventDefault();
            this.addSubtask();
        }
    }
}
