import { inject, Injectable, signal } from '@angular/core';
import { map, Observable, tap } from 'rxjs';
import { Task, TaskRequestBody } from '../interfaces/task.interface';
import { toTask, toSubtask } from '../mappers/task.mapper';
import { TaskService } from './task.service';

@Injectable({ providedIn: 'root' })
export class TaskStateService {

    private readonly taskService = inject(TaskService);

    private readonly _tasks = signal<Task[]>([]);
    private readonly _loading = signal(false);

    readonly tasks = this._tasks.asReadonly();
    readonly loading = this._loading.asReadonly();

    loadAll(): Observable<void> {
        this._loading.set(true);
        return this.taskService.getAll().pipe(
            tap(raw => {
                this._tasks.set(raw.map(toTask));
                this._loading.set(false);
            }),
            map(() => void 0)
        );
    }

    addTask(body: Pick<TaskRequestBody, 'title' | 'dueDate' | 'listId'>): void {
        this.taskService.createTask(body).pipe(
            tap(raw => this._tasks.update(ts => [...ts, toTask(raw)]))
        ).subscribe({ error: console.error });
    }

    updateTask(taskId: number, body: TaskRequestBody): void {
        this.taskService.updateTask(taskId, body).pipe(
            tap(raw => {
                const updated = toTask(raw);
                this._tasks.update(ts => ts.map(t => t.id === taskId ? updated : t));
            })
        ).subscribe({ error: console.error });
    }

    deleteTask(taskId: number): void {
        this.taskService.deleteTask(taskId).pipe(
            tap(() => this._tasks.update(ts => ts.filter(t => t.id !== taskId)))
        ).subscribe({ error: console.error });
    }

    addSubtask(taskId: number, body: { title: string }): void {
        this.taskService.createSubtask(taskId, body).pipe(
            tap(raw => {
                const subtask = toSubtask(raw);
                this._tasks.update(ts =>
                    ts.map(t =>
                        t.id === taskId
                            ? { ...t, subtasks: [...t.subtasks, subtask] }
                            : t
                    )
                );
            })
        ).subscribe({ error: console.error });
    }

    updateSubtask(taskId: number, subtaskId: number, body: { title: string; completed: boolean }): void {
        this.taskService.updateSubtask(taskId, subtaskId, body).pipe(
            tap(raw => {
                const updated = toSubtask(raw);
                this._tasks.update(ts =>
                    ts.map(t =>
                        t.id === taskId
                            ? { ...t, subtasks: t.subtasks.map(s => s.id === subtaskId ? updated : s) }
                            : t
                    )
                );
            })
        ).subscribe({ error: console.error });
    }

    deleteSubtask(taskId: number, subtaskId: number): void {
        this.taskService.deleteSubtask(taskId, subtaskId).pipe(
            tap(() =>
                this._tasks.update(ts =>
                    ts.map(t =>
                        t.id === taskId
                            ? { ...t, subtasks: t.subtasks.filter(s => s.id !== subtaskId) }
                            : t
                    )
                )
            )
        ).subscribe({ error: console.error });
    }
}
