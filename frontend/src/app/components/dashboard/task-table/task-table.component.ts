import { ChangeDetectionStrategy, Component, computed, effect, input, signal, untracked } from "@angular/core";
import { DatePipe } from "@angular/common";
import { TaskTableRow } from "../../../interfaces/dashboard.interface";

@Component({
    selector: 'app-task-table',
    templateUrl: './task-table.component.html',
    styleUrls: ['./task-table.component.scss'],
    imports: [DatePipe],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskTableComponent {

    readonly rows = input.required<TaskTableRow[]>();
    readonly title = input<string>('Recent activity');
    readonly subtitle = input<string>('');
    readonly pageSize = input<number>(10);

    readonly currentPage = signal(0);

    readonly totalPages = computed(() =>
        Math.max(1, Math.ceil(this.rows().length / this.pageSize()))
    );

    readonly paginatedRows = computed(() => {
        const start = this.currentPage() * this.pageSize();
        return this.rows().slice(start, start + this.pageSize());
    });

    readonly paginationItems = computed<(number | null)[]>(() => {
        const total = this.totalPages();
        const current = this.currentPage();
        if (total <= 7) {
            return Array.from({ length: total }, (_, i) => i);
        }
        const items: (number | null)[] = [0];
        if (current > 3) items.push(null);
        for (let i = Math.max(1, current - 2); i <= Math.min(total - 2, current + 2); i++) {
            items.push(i);
        }
        if (current < total - 4) items.push(null);
        items.push(total - 1);
        return items;
    });

    constructor() {
        effect(() => {
            this.rows();
            untracked(() => this.currentPage.set(0));
        });
    }

    previousPage(): void {
        if (this.currentPage() > 0) {
            this.currentPage.update(p => p - 1);
        }
    }

    nextPage(): void {
        if (this.currentPage() < this.totalPages() - 1) {
            this.currentPage.update(p => p + 1);
        }
    }

    goToPage(page: number): void {
        this.currentPage.set(page);
    }
}
