import { Component, input, output, signal, viewChild } from "@angular/core";
import { QuickTask, TaskListTemplateColor, TaskListTemplateSchedule, TaskRequestBody } from "../../interfaces/task.interface";
import { RruleDay, RruleFrequency } from "../../interfaces/rrule.interface";
import { ModalComponent } from "../../shared/components/modal/modal.component";
import { InputComponent } from "../../shared/components/input/input.component";
import { Trash, LucideAngularModule, Plus } from "lucide-angular";
import { ButtonComponent } from "../../shared/components/button/button.component";
import { SelectComponent } from "../../shared/components/select/select.component";
import { SelectOption } from "../../shared/interfaces/select.interface";
import { DatepickerComponent } from "../../shared/components/datepicker/datepicker.component";

@Component({
    selector: 'app-new-template-modal',
    templateUrl: './new-template-modal.component.html',
    styleUrls: ['./new-template-modal.component.scss'],
    imports: [ModalComponent, InputComponent, LucideAngularModule, ButtonComponent, SelectComponent, DatepickerComponent]
})
export class NewTemplateModalComponent {

    // Input / output signals
    readonly isOpen = input.required<boolean>();
    readonly close = output<void>();
    readonly created = output<void>(); // signifies creation of template

    // Internal signals
    readonly templateName = signal<string>('');
    readonly templateColor = signal<TaskListTemplateColor>(TaskListTemplateColor.BLUE);
    readonly templateTasks = signal<QuickTask[]>([{ id: crypto.randomUUID(), title: '', notes: '' }]);
    readonly templateScheduleType = signal<TaskListTemplateSchedule>(TaskListTemplateSchedule.RECURRING);
    readonly templateOneTimeDate = signal<Date>(new Date());
    readonly templateFrequency = signal<RruleFrequency>(RruleFrequency.WEEKLY);
    readonly dayOfWeek = signal<RruleDay>(RruleDay.MONDAY);
    readonly monthDay = signal<number>(1);
    readonly submitting = signal<boolean>(false);

    readonly exhaustiveTemplateColors = signal<TaskListTemplateColor[]>(
        Object.values(TaskListTemplateColor)
    );
    readonly scheduleTypes = signal<SelectOption[]>(
        [
            { value: TaskListTemplateSchedule.RECURRING, label: 'Recurring' },
            // { value: TaskListTemplateSchedule.ONE_TIME, label: 'One-time' }
        ]
    );
    readonly frequencies = signal<SelectOption[]>(
        [
            { value: RruleFrequency.DAILY, label: 'Daily' },
            { value: RruleFrequency.WEEKLY, label: 'Weekly' },
            { value: RruleFrequency.MONTHLY, label: 'Monthly' }
        ]
    );
    readonly daysOfWeek = signal<SelectOption[]>(
        [
            { value: RruleDay.MONDAY, label: 'Monday' },
            { value: RruleDay.TUESDAY, label: 'Tuesday' },
            { value: RruleDay.WEDNESDAY, label: 'Wednesday' },
            { value: RruleDay.THURSDAY, label: 'Thursday' },
            { value: RruleDay.FRIDAY, label: 'Friday' },
            { value: RruleDay.SATURDAY, label: 'Saturday' },
            { value: RruleDay.SUNDAY, label: 'Sunday' }
        ]
    );
    readonly daysOfMonth = signal<SelectOption[]>(
        [...Array(31).keys()].map(num => ({ value: num + 1, label: `${num + 1}` }))
    );

    readonly templateOneTimeDatepicker = viewChild<DatepickerComponent>('templateOneTimeDatepicker');

    // Lucide Icons
    readonly trash = Trash;
    readonly plus = Plus;

    selectColor(color: TaskListTemplateColor) {
        this.templateColor.set(color);
    }

    updateTaskTitle(index: number, title: string) {
        this.templateTasks.update(tasks =>
            tasks.map((task, i) => i === index ? { ...task, title } : task)
        );
    }

    updateTaskNotes(index: number, notes: string) {
        this.templateTasks.update(tasks =>
            tasks.map((task, i) => i === index ? { ...task, notes } : task)
        );
    }

    addTask() {
        this.templateTasks.update(tasks =>
            [ ...tasks, { id: crypto.randomUUID(), title: '', notes: '' }]
        );
    }

    deleteTask(index: number) {
        this.templateTasks.update(tasks =>
            tasks.filter((_, i) => i !== index)
        );
    }

}