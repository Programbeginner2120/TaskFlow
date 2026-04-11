import { Component, computed, ElementRef, HostListener, inject, input, model, signal, viewChild } from '@angular/core';
import { LucideAngularModule, Calendar, ChevronLeft, ChevronRight } from 'lucide-angular';

export interface CalendarDay {
    date: Date;
    inMonth: boolean;
    isToday: boolean;
    isSelected: boolean;
    isDisabled: boolean;
}

@Component({
    selector: 'app-datepicker',
    templateUrl: './datepicker.component.html',
    styleUrls: ['./datepicker.component.scss'],
    imports: [LucideAngularModule],
})
export class DatepickerComponent {
    private readonly elementRef = inject(ElementRef);

    readonly calendarIcon = Calendar;
    readonly chevronLeftIcon = ChevronLeft;
    readonly chevronRightIcon = ChevronRight;

    value = model<Date | null>(null);
    disabled = input<boolean>(false);
    minDate = input<Date | null>(null);
    maxDate = input<Date | null>(null);

    private readonly triggerRef = viewChild<ElementRef<HTMLButtonElement>>('trigger');

    isOpen = signal<boolean>(false);
    viewMonth = signal<number>(new Date().getMonth());
    viewYear = signal<number>(new Date().getFullYear());
    popupPosition = signal<{ vertical: 'below' | 'above'; horizontal: 'end' | 'start' }>({
        vertical: 'below',
        horizontal: 'end',
    });

    private readonly POPUP_HEIGHT = 300;
    private readonly POPUP_WIDTH = 256;

    readonly headerLabel = computed(() => {
        const date = new Date(this.viewYear(), this.viewMonth(), 1);
        return date.toLocaleDateString('en-US', { month: 'long', year: 'numeric' });
    });

    readonly calendarDays = computed<CalendarDay[]>(() => {
        const month = this.viewMonth();
        const year = this.viewYear();
        const selected = this.value();
        const today = new Date();

        const firstOfMonth = new Date(year, month, 1);
        const lastOfMonth = new Date(year, month + 1, 0);

        // Day of week the month starts on (0=Sunday)
        const startPad = firstOfMonth.getDay();
        // Day of week the month ends on — fill to end of last week row
        const endPad = 6 - lastOfMonth.getDay();

        const days: CalendarDay[] = [];

        // Padding days from previous month
        for (let i = startPad - 1; i >= 0; i--) {
            const date = new Date(year, month, -i);
            days.push({ date, inMonth: false, isToday: false, isSelected: false, isDisabled: false });
        }

        // Days in current month
        for (let d = 1; d <= lastOfMonth.getDate(); d++) {
            const date = new Date(year, month, d);
            days.push({
                date,
                inMonth: true,
                isToday: this.isSameDay(date, today),
                isSelected: selected !== null && this.isSameDay(date, selected),
                isDisabled: this.isDateDisabled(date),
            });
        }

        // Padding days from next month
        for (let i = 1; i <= endPad; i++) {
            const date = new Date(year, month + 1, i);
            days.push({ date, inMonth: false, isToday: false, isSelected: false, isDisabled: false });
        }

        return days;
    });

    private isSameDay(a: Date, b: Date): boolean {
        return (
            a.getFullYear() === b.getFullYear() &&
            a.getMonth() === b.getMonth() &&
            a.getDate() === b.getDate()
        );
    }

    /** Returns true only if `date` is strictly before `bound` at day granularity. */
    private isDayBefore(date: Date, bound: Date): boolean {
        if (date.getFullYear() !== bound.getFullYear()) return date.getFullYear() < bound.getFullYear();
        if (date.getMonth() !== bound.getMonth()) return date.getMonth() < bound.getMonth();
        return date.getDate() < bound.getDate();
    }

    private isDateDisabled(date: Date): boolean {
        const min = this.minDate();
        const max = this.maxDate();
        if (min !== null && this.isDayBefore(date, min)) return true;
        if (max !== null && this.isDayBefore(max, date)) return true;
        return false;
    }

    toggle(event: Event): void {
        event.stopPropagation();
        if (this.disabled()) return;

        if (!this.isOpen()) {
            // Initialise the view to the selected date (or today)
            const ref = this.value() ?? new Date();
            this.viewMonth.set(ref.getMonth());
            this.viewYear.set(ref.getFullYear());
            this.computePosition();
        }

        this.isOpen.update(v => !v);
    }

    private computePosition(): void {
        const triggerEl = this.triggerRef()?.nativeElement;
        if (!triggerEl) return;

        const rect = triggerEl.getBoundingClientRect();
        const spaceBelow = window.innerHeight - rect.bottom;
        const spaceAbove = rect.top;

        const vertical =
            spaceBelow >= this.POPUP_HEIGHT || spaceBelow >= spaceAbove ? 'below' : 'above';

        // Default (end) aligns popup right edge to trigger right edge, extending left (256px).
        // Flip to start (left: 0) when there isn't enough space to the left.
        const horizontal = rect.right >= this.POPUP_WIDTH ? 'end' : 'start';

        this.popupPosition.set({ vertical, horizontal });
    }

    prevMonth(event: Event): void {
        event.stopPropagation();
        const m = this.viewMonth();
        if (m === 0) {
            this.viewMonth.set(11);
            this.viewYear.update(y => y - 1);
        } else {
            this.viewMonth.update(v => v - 1);
        }
    }

    nextMonth(event: Event): void {
        event.stopPropagation();
        const m = this.viewMonth();
        if (m === 11) {
            this.viewMonth.set(0);
            this.viewYear.update(y => y + 1);
        } else {
            this.viewMonth.update(v => v + 1);
        }
    }

    selectDate(day: CalendarDay, event: Event): void {
        event.stopPropagation();
        if (!day.inMonth || day.isDisabled) return;
        this.value.set(new Date(day.date));
        this.isOpen.set(false);
    }

    clearDate(event: Event): void {
        event.stopPropagation();
        this.value.set(null);
        this.isOpen.set(false);
    }

    @HostListener('document:click', ['$event'])
    onDocumentClick(event: MouseEvent): void {
        if (!this.elementRef.nativeElement.contains(event.target)) {
            this.isOpen.set(false);
        }
    }

    @HostListener('keydown', ['$event'])
    onKeyDown(event: KeyboardEvent): void {
        if (event.key === 'Escape' && this.isOpen()) {
            this.isOpen.set(false);
        }
    }
}
