import { Component, input, output, signal } from "@angular/core";
import { QuickTask, TaskListTemplateColor, TaskListTemplateSchedule, TaskRequestBody } from "../../interfaces/task.interface";
import { RruleDay, RruleFrequency } from "../../interfaces/rrule.interface";
import { ModalComponent } from "../../shared/components/modal/modal.component";
import { InputComponent } from "../../shared/components/input/input.component";
import { Trash, LucideAngularModule, Plus } from "lucide-angular";
import { ButtonComponent } from "../../shared/components/button/button.component";

@Component({
    selector: 'app-new-template-modal',
    templateUrl: './new-template-modal.component.html',
    styleUrls: ['./new-template-modal.component.scss'],
    imports: [ModalComponent, InputComponent, LucideAngularModule, ButtonComponent]
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
    readonly templateScheduleType = signal<TaskListTemplateSchedule>('recurring');
    readonly templateFrequency = signal<RruleFrequency>('WEEKLY');
    readonly dayOfWeek = signal<RruleDay>('MO');
    readonly monthDay = signal<number>(1);
    readonly submitting = signal<boolean>(false);

    readonly exhaustiveTemplateColors = signal<TaskListTemplateColor[]>(
        Object.values(TaskListTemplateColor)
    );

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