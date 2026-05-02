import { Component, input, output, signal } from "@angular/core";
import { TaskListTemplateColor, TaskListTemplateSchedule, TaskRequestBody } from "../../interfaces/task.interface";
import { RruleDay, RruleFrequency } from "../../interfaces/rrule.interface";
import { ModalComponent } from "../../shared/components/modal/modal.component";
import { InputComponent } from "../../shared/components/input/input.component";
import { NgClass } from "../../../../node_modules/@angular/common/types/_common_module-chunk";

@Component({
    selector: 'app-new-template-modal',
    templateUrl: './new-template-modal.component.html',
    styleUrls: ['./new-template-modal.component.scss'],
    imports: [ModalComponent, InputComponent]
})
export class NewTemplateModalComponent {

    // Input / output signals
    readonly isOpen = input.required<boolean>();
    readonly close = output<void>();
    readonly created = output<void>(); // signifies creation of template

    // Internal signals
    readonly templateName = signal<string>('');
    readonly templateColor = signal<TaskListTemplateColor>(TaskListTemplateColor.BLUE);
    readonly templateTasks = signal<Pick<TaskRequestBody, 'title' | 'notes'>[]>([{ title: '', notes: '' }]);
    readonly templateScheduleType = signal<TaskListTemplateSchedule>('recurring');
    readonly templateFrequency = signal<RruleFrequency>('WEEKLY');
    readonly dayOfWeek = signal<RruleDay>('MO');
    readonly monthDay = signal<number>(1);
    readonly submitting = signal<boolean>(false);

    readonly exhaustiveTemplateColors = signal<string[]>(
        Object.values(TaskListTemplateColor)
    );

}