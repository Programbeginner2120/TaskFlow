import { LucideIconData } from "lucide-angular";

export interface StatisticsCard {
    label: string;
    value: number;
    iconBackgroundColor: string;
    icon: LucideIconData;
}

export type TaskStatus = 'Active' | 'Completed' | 'Overdue';

export interface TaskTableRow {
    title: string;
    listName: string | null;
    listColor: string | null;
    dueDate: Date | null;
    status: TaskStatus;
}