import {
    ChangeDetectionStrategy,
    Component,
    input,
    OnDestroy,
    output,
} from '@angular/core';
import { LucideAngularModule, X } from 'lucide-angular';
import { Task } from '../../interfaces/task.interface';

@Component({
    selector: 'app-calendar-overflow-modal',
    templateUrl: './calendar-overflow-modal.component.html',
    styleUrls: ['./calendar-overflow-modal.component.scss'],
    imports: [LucideAngularModule],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CalendarOverflowModalComponent implements OnDestroy {
    readonly closeIcon = X;

    tasks = input.required<Task[]>();
    date = input<Date | null>(null);

    taskSelected = output<Task>();
    close = output<void>();

    private readonly keydownHandler = (e: KeyboardEvent) => {
        if (e.key === 'Escape') this.close.emit();
    };

    constructor() {
        document.addEventListener('keydown', this.keydownHandler);
    }

    ngOnDestroy(): void {
        document.removeEventListener('keydown', this.keydownHandler);
    }

    onBackdropClick(): void {
        this.close.emit();
    }

    onTaskClick(task: Task): void {
        this.taskSelected.emit(task);
        this.close.emit();
    }
}
