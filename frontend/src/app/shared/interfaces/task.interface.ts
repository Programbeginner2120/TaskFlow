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
