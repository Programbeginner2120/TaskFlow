import { Component, computed, inject, model, signal, Signal } from "@angular/core";
import { SelectComponent } from "../../../shared/components/select/select.component";
import { MultiSelectComponent } from "../../../shared/components/multi-select/multi-select.component";
import { MultiSelectItemComponent } from "../../../shared/components/multi-select/multi-select-item/multi-select-item.component";
import { SelectOption } from "../../../shared/interfaces/select.interface";
import { TaskListStateService } from "../../../services/task-list-state.service";

@Component({
    selector: 'app-dashboard-filter',
    templateUrl: './dashboard-filter.component.html',
    styleUrls: ['./dashboard-filter.component.scss'],
    imports: [SelectComponent, MultiSelectComponent, MultiSelectItemComponent]
})
export class DashboardFilterComponent {

    // Service injection
    private readonly taskListStateService = inject(TaskListStateService);

    readonly durationSelectOptions: Signal<SelectOption[]> = signal([
        { value: 'LAST_7_DAYS', label: 'Last 7 Days' },
        { value: 'LAST_30_DAYS', label: 'Last 30 Days' },
        { value: 'LAST_90_DAYS', label: 'Last 90 Days' },
        { value: 'ALL_TIME', label: 'All Time' }
    ]);

    readonly statusSelectOptions: Signal<SelectOption[]> = signal([
        { value: 'ALL', label: 'All Status' },
        { value: 'ACTIVE', label: 'Active' },
        { value: 'COMPLETED', label: 'Completed' },
    ]);

    readonly taskLists = computed(() =>
        this.taskListStateService.lists()
    );

    durationSelection = model('');
    statusSelection = model('');
    listSelections = model<number[]>([]);

}