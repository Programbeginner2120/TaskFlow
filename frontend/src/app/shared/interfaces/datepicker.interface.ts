export interface CalendarDay {
    date: Date;
    inMonth: boolean;
    isToday: boolean;
    isSelected: boolean;
    isDisabled: boolean;
}

export type DatepickerPosition =
    | 'bottom-right'
    | 'bottom-left'
    | 'top-right'
    | 'top-left'
    | 'right'
    | 'left'
    | 'bottom'
    | 'top';