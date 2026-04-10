export interface Subtask {
    id: number;
    taskId: number;
    title: string;
    completed: boolean;
}

export interface Task {
    id: number;
    userId: number;
    title: string;
    completed: boolean;
    dueDate: Date | null;
    listId: number;
    notes: string;
    subtasks: Subtask[];
    createdAt: Date;
}

export interface TaskList {
    id: number;
    userId: number;
    name: string;
    color: string;
}
