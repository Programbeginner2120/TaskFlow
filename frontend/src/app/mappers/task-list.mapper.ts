import { TaskList } from '../interfaces/task.interface';
import { TaskListApiResponse } from '../interfaces/task.interface';

export function toTaskList(raw: TaskListApiResponse): TaskList {
    return {
        id: raw.id,
        userId: raw.userId,
        name: raw.name,
        color: raw.color,
        createdAt: new Date(raw.createdAt),
        updatedAt: raw.updatedAt ? new Date(raw.updatedAt) : null,
    };
}
