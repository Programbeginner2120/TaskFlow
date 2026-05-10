import { ChangeDetectionStrategy, Component, OnInit, computed, inject, input, output, signal, viewChild } from "@angular/core";
import { CreateTaskListTemplateRequest, CreateTaskTemplateRequest, TaskListTemplate, TaskListTemplateColor, TaskListTemplateSchedule, UpdateTaskListTemplateRequest } from "../../interfaces/task.interface";
import { RruleDay, RruleFrequency } from "../../interfaces/rrule.interface";
import { ModalComponent } from "../../shared/components/modal/modal.component";
import { InputComponent } from "../../shared/components/input/input.component";
import { Trash, LucideAngularModule, Plus } from "lucide-angular";
import { ButtonComponent } from "../../shared/components/button/button.component";
import { SelectComponent } from "../../shared/components/select/select.component";
import { SelectOption } from "../../shared/interfaces/select.interface";
import { DatepickerComponent } from "../../shared/components/datepicker/datepicker.component";
import { TaskListTemplateStateService } from "../../services/task-list-template-state.service";
import { buildRule, parseRule } from "../../utils/rrule.util";

@Component({
    selector: 'app-new-template-modal',
    templateUrl: './new-template-modal.component.html',
    styleUrls: ['./new-template-modal.component.scss'],
    imports: [ModalComponent, InputComponent, LucideAngularModule, ButtonComponent, SelectComponent, DatepickerComponent],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NewTemplateModalComponent implements OnInit {

    // Input / output signals
    readonly template = input<TaskListTemplate | null>(null);
    readonly close = output<void>();
    readonly created = output<void>(); // signifies creation of template

    // Injections
    private readonly taskListTemplateStateService = inject(TaskListTemplateStateService);

    // Internal signals
    readonly templateName = signal<string>('');
    readonly templateGenerationTitle = signal<string>('');
    readonly templateColor = signal<TaskListTemplateColor>(TaskListTemplateColor.BLUE);
    readonly templateTasks = signal<CreateTaskTemplateRequest[]>([{ title: '', notes: '', dueDateOffset: null, subtaskTemplates: [] }]);
    readonly templateScheduleType = signal<TaskListTemplateSchedule>(TaskListTemplateSchedule.RECURRING);
    readonly templateOneTimeDate = signal<Date>(new Date());
    readonly templateFrequency = signal<RruleFrequency>(RruleFrequency.WEEKLY);
    readonly dayOfWeek = signal<RruleDay>(RruleDay.MONDAY);
    readonly monthDay = signal<number>(1);
    readonly submitting = signal<boolean>(false);
    readonly submitted = signal<boolean>(false);

    // Edit mode
    readonly isEditMode = computed<boolean>(() => this.template() !== null);

    // Computed validation
    readonly templateNameError = computed<string | undefined>(() => {
        if (!this.submitted()) return undefined;
        return this.templateName().trim() === '' ? 'Template name is required' : undefined;
    });

    readonly taskTitleErrors = computed<(string | undefined)[]>(() => {
        if (!this.submitted()) return this.templateTasks().map(() => undefined);
        return this.templateTasks().map(task =>
            task.title.trim() === '' ? 'Task title is required' : undefined
        );
    });

    readonly isFormValid = computed<boolean>(() =>
        this.templateName().trim() !== '' &&
        this.templateTasks().every(task => task.title.trim() !== '')
    );

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

    readonly generationTitlePreview = computed<string>(() => {
        const title = this.templateGenerationTitle().trim();
        if (!title) return '';
        const today = new Date();
        return title
            .replace('YYYY', String(today.getFullYear()).padStart(4, '0'))
            .replace('MM',   String(today.getMonth() + 1).padStart(2, '0'))
            .replace('DD',   String(today.getDate()).padStart(2, '0'));
    });

    // Lucide Icons
    readonly trash = Trash;
    readonly plus = Plus;

    selectColor(color: TaskListTemplateColor) {
        this.templateColor.set(color);
    }

    ngOnInit(): void {
        const t = this.template();
        if (!t) return; // create mode — defaults already set in signal declarations

        this.templateName.set(t.name);
        if (t.generationTitle) this.templateGenerationTitle.set(t.generationTitle);
        this.templateColor.set(t.color as TaskListTemplateColor);
        this.templateTasks.set(
            t.taskTemplates.map(tt => ({
                title: tt.title,
                notes: tt.notes ?? '',
                dueDateOffset: tt.dueDateOffset,
                subtaskTemplates: tt.subtaskTemplates.map(st => ({ title: st.title })),
            }))
        );
        const parsed = parseRule(t.rrule);
        this.templateFrequency.set(parsed.frequency);
        if (parsed.dayOfWeek) this.dayOfWeek.set(parsed.dayOfWeek);
        if (parsed.monthDay)  this.monthDay.set(parsed.monthDay);
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

    updateTaskDueDateOffset(index: number, dueDateOffset: string) {
        const parsed = dueDateOffset.trim();
        const dueDateOffsetParsed = parsed === '' ? null : Number(parsed);
        this.templateTasks.update(tasks =>
            tasks.map((task, i) => i === index ? { ...task, dueDateOffset: dueDateOffsetParsed } : task)
        );
    }

    addTask() {
        this.templateTasks.update(tasks =>
            [ ...tasks, { title: '', notes: '', dueDateOffset: null, subtaskTemplates: [] }]
        );
    }

    deleteTask(index: number) {
        this.templateTasks.update(tasks =>
            tasks.filter((_, i) => i !== index)
        );
    }

    submitTemplate() {
        this.submitted.set(true);
        if (!this.isFormValid()) return;

        const scheduleType: string = this.templateScheduleType();
        if (TaskListTemplateSchedule.RECURRING === scheduleType) {

            const frequency = this.templateFrequency();
            let rrule: string;
            if (RruleFrequency.DAILY === frequency) {
                rrule = buildRule(this.templateFrequency());
            } else if (RruleFrequency.WEEKLY === frequency) {
                rrule = buildRule(this.templateFrequency(), this.dayOfWeek());
            } else if (RruleFrequency.MONTHLY === frequency) {
                rrule = buildRule(this.templateFrequency(), undefined, this.monthDay())
            } else {
                throw new Error('Rrule frequency could not be processed');
            }

            if (this.isEditMode()) {
                const updateRequest: UpdateTaskListTemplateRequest = {
                    name: this.templateName(),
                    color: this.templateColor(),
                    generationTitle: this.templateGenerationTitle() || null,
                    rrule,
                    timezone: Intl.DateTimeFormat().resolvedOptions().timeZone,
                    taskTemplates: this.templateTasks()
                };
                this.taskListTemplateStateService.updateTemplate(this.template()!.id, updateRequest);
            } else {
                const createRequest: CreateTaskListTemplateRequest = {
                    name: this.templateName(),
                    color: this.templateColor(),
                    generationTitle: this.templateGenerationTitle() || null,
                    rrule,
                    timezone: Intl.DateTimeFormat().resolvedOptions().timeZone,
                    taskTemplates: this.templateTasks(),
                };
                this.taskListTemplateStateService.addTemplate(createRequest);
            }

            this.close.emit();
            
        } else if (TaskListTemplateSchedule.ONE_TIME === scheduleType) {
            console.log("TODO: implement this flow, does nothing right now...");
        } else {
            throw Error('Schedule type could not be processed');
        }
    }

}