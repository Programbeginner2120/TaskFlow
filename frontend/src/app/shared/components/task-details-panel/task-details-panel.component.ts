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
import { TaskService } from '../../../services/task.service';
import { TaskListService } from '../../../services/task-list.service';
import { SelectComponent } from '../select/select.component';
import { DatepickerComponent } from '../datepicker/datepicker.component';
import { SelectOption } from '../../interfaces/select.interface';
import { PlatformService } from '../../../services/platform.service';

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

    private readonly taskService = inject(TaskService);
    private readonly taskListService = inject(TaskListService);
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
                this.taskService.updateTask(t.id, { dueDate: date }).subscribe();
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
        if (value) {
            this.taskService.updateTask(t.id, { title: value }).subscribe();
        }
    }

    onNotesChange(event: Event): void {
        const t = this.task();
        if (!t) return;
        const value = (event.target as HTMLTextAreaElement).value;
        this.taskService.updateTask(t.id, { notes: value }).subscribe();
    }

    onListChange(value: string | number | null): void {
        const t = this.task();
        if (!t || value === null) return;
        this.taskService.updateTask(t.id, { listId: value as number }).subscribe();
    }

    toggleSubtask(subtaskId: number): void {
        const t = this.task();
        if (!t) return;
        this.taskService.toggleSubtaskCompletion(t.id, subtaskId).subscribe();
    }

    addSubtask(): void {
        const t = this.task();
        const title = this.newSubtaskTitle().trim();
        if (!t || !title) return;
        this.taskService.addSubtask(t.id, title).subscribe(() => {
            this.newSubtaskTitle.set('');
        });
    }

    onSubtaskKeydown(event: KeyboardEvent): void {
        if (event.key === 'Enter') {
            event.preventDefault();
            this.addSubtask();
        }
    }
}
