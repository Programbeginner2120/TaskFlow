import { Task, TaskApiResponse } from '../interfaces/task.interface';
import { toTask } from './task.mapper';

export function toDashboardTask(raw: TaskApiResponse): Task {
    return toTask(raw);
}
