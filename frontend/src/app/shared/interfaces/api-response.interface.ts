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
