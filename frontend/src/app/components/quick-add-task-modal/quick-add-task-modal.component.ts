import { ChangeDetectionStrategy, Component, computed, inject, input, output, signal, viewChild } from "@angular/core";
import { ModalComponent } from "../../shared/components/modal/modal.component";
import { TaskListStateService } from "../../services/task-list-state.service";
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from "rxjs";
import { SelectOption } from "../../shared/interfaces/select.interface";
import { InputComponent } from "../../shared/components/input/input.component";
import { DatepickerComponent } from "../../shared/components/datepicker/datepicker.component";
import { ButtonComponent } from "../../shared/components/button/button.component";
import { SelectComponent } from "../../shared/components/select/select.component";
import { PlatformService } from "../../services/platform.service";

@Component({
    selector: 'app-quick-add-task-modal',
    templateUrl: './quick-add-task-modal.component.html',
    styleUrls: ['./quick-add-task-modal.component.scss'],
    imports: [ModalComponent, InputComponent, DatepickerComponent, ButtonComponent, SelectComponent],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class QuickAddTaskModalComponent {

    // Inputs
    readonly startingDueDate = input<Date | null>();

    // Outputs
    readonly close = output<void>();

    // View children
    readonly dueDateDatepicker = viewChild<DatepickerComponent>('dueDateDatePicker');

    private readonly taskListService = inject(TaskListStateService);
    readonly platformService = inject(PlatformService);

    readonly sortedListOptions = computed(() => {
        const taskLists = this.taskListService.lists();
        return [...taskLists]
            .sort((a, b) => a.name.localeCompare(b.name))
            .map(list => {
                return {
                    label: list.name,
                    value: list.id
                } as SelectOption;
            })
    });

    // Form fields
    readonly taskTitle = signal<string>('');
    readonly taskDueDate = signal<Date>(this.startingDueDate() ?? new Date());
    readonly taskListId = signal<number | null>(null);

}