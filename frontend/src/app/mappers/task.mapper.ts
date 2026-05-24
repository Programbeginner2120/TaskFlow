import { Task, Subtask } from '../interfaces/task.interface';
import { TaskApiResponse, SubtaskApiResponse } from '../interfaces/task.interface';

export function toSubtask(raw: SubtaskApiResponse): Subtask {
    return {
        id: raw.id,
        taskId: raw.taskId,
        title: raw.title,
        completed: raw.completed,
        createdAt: new Date(raw.createdAt),
        updatedAt: raw.updatedAt ? new Date(raw.updatedAt) : null,
    };
}

export function toTask(raw: TaskApiResponse): Task {
    return {
        id: raw.id,
        userId: raw.userId,
        listId: raw.listId,
        title: raw.title,
        notes: raw.notes ?? '',
        completed: raw.completed,
        completedAt: raw.completedAt ? new Date(raw.completedAt) : null,
        dueDate: raw.dueDate ? new Date(raw.dueDate + 'T00:00:00') : null,
        subtasks: raw.subtasks.map(toSubtask),
        position: raw.position,
        createdAt: new Date(raw.createdAt),
        updatedAt: raw.updatedAt ? new Date(raw.updatedAt) : null,
    };
}
