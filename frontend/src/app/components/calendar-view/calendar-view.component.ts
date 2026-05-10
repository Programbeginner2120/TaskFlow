import {
    ChangeDetectionStrategy,
    Component,
    computed,
    inject,
    input,
    output,
    signal,
} from '@angular/core';
import { FullCalendarModule } from '@fullcalendar/angular';
import type { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { Task } from '../../interfaces/task.interface';
import { TaskStateService } from '../../services/task-state.service';
import { PlatformService } from '../../services/platform.service';
import { CalendarOverflowModalComponent } from '../calendar-overflow-modal/calendar-overflow-modal.component';

@Component({
    selector: 'app-calendar-view',
    templateUrl: './calendar-view.component.html',
    styleUrls: ['./calendar-view.component.scss'],
    imports: [FullCalendarModule, CalendarOverflowModalComponent],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CalendarViewComponent {
    private readonly taskService = inject(TaskStateService);
    readonly platformService = inject(PlatformService);

    searchQuery = input<string>('');
    taskSelected = output<Task>();

    // ── Derived event list ────────────────────────────────────────────────────
    readonly calendarEvents = computed(() =>
        this.taskService
            .tasks()
            .filter((t) => t.dueDate !== null)
            .filter((t) => {
                const q = this.searchQuery().toLowerCase().trim();
                return !q || t.title.toLowerCase().includes(q);
            })
            .map((t) => ({
                id: String(t.id),
                title: t.title,
                start: t.dueDate!,
                allDay: true,
                classNames: t.completed ? ['fc-event--completed'] : [],
                extendedProps: { task: t },
            }))
    );

    // ── Overflow modal state ──────────────────────────────────────────────────
    overflowModalOpen = signal<boolean>(false);
    overflowTasks = signal<Task[]>([]);
    overflowDate = signal<Date | null>(null);

    // ── Calendar options (reactive to platform) ───────────────────────────────
    readonly calendarOptions = computed<CalendarOptions>(() => {
        const isPhone = this.platformService.isPhone();

        return {
            plugins: [dayGridPlugin, interactionPlugin],
            initialView: 'dayGridMonth',
            headerToolbar: {
                left: isPhone ? 'prev,next' : 'prev,next today',
                center: 'title',
                right: isPhone ? 'today' : 'dayGridMonth,dayGridWeek',
            },
            height: 'auto',
            dayMaxEvents: isPhone ? 1 : 2,

            // Custom "+N more" click — opens app-native modal
            moreLinkClick: (arg: any) => {
                this.overflowDate.set(arg.date as Date);
                this.overflowTasks.set(
                    arg.allSegs.map((s: any) => s.event.extendedProps['task'] as Task)
                );
                this.overflowModalOpen.set(true);
                return 'stop'; // prevent FullCalendar's default popover
            },

            // Provide events as a function so FullCalendar always gets the latest
            events: this.calendarEvents(),

            eventClick: (arg: any) => {
                const task = arg.event.extendedProps['task'] as Task;
                this.taskSelected.emit(task);
            },

            eventClassNames: ['fc-task-event'],
        } as CalendarOptions;
    });

    onTaskSelected(task: Task): void {
        this.taskSelected.emit(task);
        this.overflowModalOpen.set(false);
    }

    onOverflowClose(): void {
        this.overflowModalOpen.set(false);
    }
}
