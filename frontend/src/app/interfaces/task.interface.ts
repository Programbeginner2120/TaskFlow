export interface Subtask {
    id: number;
    taskId: number;
    title: string;
    completed: boolean;
    createdAt: Date;
    updatedAt: Date | null;
}

export interface Task {
    id: number;
    userId: number;
    title: string;
    completed: boolean;
    dueDate: Date | null;
    listId: number | null;
    notes: string;
    subtasks: Subtask[];
    createdAt: Date;
    updatedAt: Date | null;
}

export interface TaskList {
    id: number;
    userId: number;
    name: string;
    color: string;
    createdAt: Date;
    updatedAt: Date | null;
}

export interface TaskRequestBody {
    title: string;
    notes?: string | null;
    dueDate?: string | null;
    listId?: number | null;
    completed?: boolean;
}

export interface QuickTask {
    id: string;
    title: string;
    notes: string;
}

// Raw shapes returned by the API — date fields are strings as serialized by Spring

export interface SubtaskApiResponse {
    id: number;
    taskId: number;
    title: string;
    completed: boolean;
    createdAt: string;
    updatedAt: string | null;
}

export interface TaskApiResponse {
    id: number;
    userId: number;
    listId: number | null;
    title: string;
    notes: string | null;
    completed: boolean;
    dueDate: string | null;         // "YYYY-MM-DD"
    subtasks: SubtaskApiResponse[];
    createdAt: string;
    updatedAt: string | null;
}

export interface TaskListApiResponse {
    id: number;
    userId: number;
    name: string;
    color: string;
    createdAt: string;
    updatedAt: string | null;
}

export interface TaskTemplate {
    id: number;
    listTemplateId: number;
    title: string;
    notes: string | null;
    dueDateOffset: number | null;
    createdAt: Date;
    updatedAt: Date | null;
    subtaskTemplates: { id: number; title: string }[];
}

export interface TaskListTemplate {
    id: number;
    userId: number;
    name: string;
    color: string;
    rrule: string;
    timezone: string;
    lastGenerated: Date | null;
    nextGenerate: Date | null;
    createdAt: Date;
    updatedAt: Date | null;
    taskTemplates: TaskTemplate[];
}

export interface TaskTemplateApiResponse {
    id: number;
    listTemplateId: number;
    title: string;
    notes: string | null;
    dueDateOffset: number | null;
    createdAt: string;
    updatedAt: string | null;
    subtaskTemplates: { id: number; title: string }[];
}

export interface TaskListTemplateApiResponse {
    id: number;
    userId: number;
    name: string;
    color: string;
    rrule: string;
    timezone: string;
    lastGenerated: string | null;
    nextGenerate: string | null;
    createdAt: string;
    updatedAt: string | null;
    taskTemplates: TaskTemplateApiResponse[];
}

export interface CreateTaskListTemplateRequest {
    name: string;
    color: string;
    rrule: string;
    timezone: string;
    taskTemplates: QuickTask[];
}

export interface UpdateTaskListTemplateRequest {
    name: string;
    color: string;
    rrule: string;
    timezone: string;
    taskTemplates: QuickTask[];
}

export enum TaskListTemplateColor {
    BLUE = '#6366F1',
    PINK = '#EC4899',
    ORANGE = '#F59E0B',
    GREEN = '#10B981',
    LIGHT_BLUE = '#3B82F6',
    PURPLE = '#8B5CF6',
    RED = '#EF4444',
    LIGHT_GREEN = '#14B8A6'
}

export type TaskListTemplateSchedule = 'recurring';