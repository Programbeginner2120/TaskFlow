import { LucideIconData } from "lucide-angular";
import { TaskApiResponse } from "./task.interface";

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

export type TaskDataDuration = 'LAST_7_DAYS' | 'LAST_30_DAYS' | 'LAST_90_DAYS' | 'ALL_TIME';
export type TaskDataStatus   = 'ACTIVE' | 'COMPLETED' | 'ALL';

export interface DashboardAnalyticsRequest {
    durationSelection: TaskDataDuration;
    statusSelection:   TaskDataStatus;
    listSelections:    number[];
}

export interface DashboardAnalyticsApiResponse {
    tasks: TaskApiResponse[];
}