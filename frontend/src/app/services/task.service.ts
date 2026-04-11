import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { map, Observable, tap } from 'rxjs';
import { Task, Subtask } from '../shared/interfaces/task.interface';
import { TaskApiResponse, SubtaskApiResponse } from '../shared/interfaces/api-response.interface';
import { toTask, toSubtask } from './task.mapper';
import { toLocalDateString } from '../utils/date.utils';

@Injectable({ providedIn: 'root' })
export class TaskService {

    private readonly http = inject(HttpClient);
    private readonly API = '/tasks';

    private readonly _tasks = signal<Task[]>([]);
    private readonly _loading = signal(false);

    readonly tasks = this._tasks.asReadonly();
    readonly loading = this._loading.asReadonly();

    // -------------------------------------------------------------------------
    // Bootstrap
    // -------------------------------------------------------------------------

    loadAll(): Observable<void> {
        this._loading.set(true);
        return this.http.get<TaskApiResponse[]>(this.API).pipe(
            tap(raw => {
                this._tasks.set(raw.map(toTask));
                this._loading.set(false);
            }),
            map(() => void 0)
        );
    }

    // -------------------------------------------------------------------------
    // Tasks
    // -------------------------------------------------------------------------

    addTask(title: string, dueDate?: Date | null, listId?: number | null): Observable<Task> {
        const body = {
            title,
            dueDate: dueDate ? toLocalDateString(dueDate) : null,
            listId: listId ?? null,
        };
        return this.http.post<TaskApiResponse>(this.API, body).pipe(
            tap(raw => this._tasks.update(ts => [...ts, toTask(raw)])),
            map(raw => toTask(raw))
        );
    }

    toggleCompletion(taskId: number): Observable<Task> {
        const task = this._tasks().find(t => t.id === taskId);
        if (!task) throw new Error(`Task ${taskId} not found in local state`);
        return this.updateTask(taskId, { completed: !task.completed });
    }

    updateTask(
        taskId: number,
        partial: Partial<Omit<Task, 'id' | 'userId' | 'createdAt' | 'subtasks'>>
    ): Observable<Task> {
        const existing = this._tasks().find(t => t.id === taskId);
        if (!existing) throw new Error(`Task ${taskId} not found in local state`);

        const body = {
            title: partial.title ?? existing.title,
            notes: partial.notes !== undefined ? partial.notes : existing.notes,
            dueDate: 'dueDate' in partial
                ? (partial.dueDate ? toLocalDateString(partial.dueDate) : null)
                : (existing.dueDate ? toLocalDateString(existing.dueDate) : null),
            listId: partial.listId !== undefined ? partial.listId : existing.listId,
            completed: partial.completed !== undefined ? partial.completed : existing.completed,
        };

        return this.http.put<TaskApiResponse>(`${this.API}/${taskId}`, body).pipe(
            tap(raw => {
                const updated = toTask(raw);
                this._tasks.update(ts => ts.map(t => t.id === taskId ? updated : t));
            }),
            map(raw => toTask(raw))
        );
    }

    deleteTask(taskId: number): Observable<void> {
        return this.http.delete<void>(`${this.API}/${taskId}`).pipe(
            tap(() => this._tasks.update(ts => ts.filter(t => t.id !== taskId)))
        );
    }

    // -------------------------------------------------------------------------
    // Subtasks
    // -------------------------------------------------------------------------

    addSubtask(taskId: number, title: string): Observable<Subtask> {
        return this.http.post<SubtaskApiResponse>(`${this.API}/${taskId}/subtasks`, { title }).pipe(
            tap(raw => {
                const subtask = toSubtask(raw);
                this._tasks.update(ts =>
                    ts.map(t =>
                        t.id === taskId
                            ? { ...t, subtasks: [...t.subtasks, subtask] }
                            : t
                    )
                );
            }),
            map(raw => toSubtask(raw))
        );
    }

    toggleSubtaskCompletion(taskId: number, subtaskId: number): Observable<Subtask> {
        const task = this._tasks().find(t => t.id === taskId);
        const subtask = task?.subtasks.find(s => s.id === subtaskId);
        if (!subtask) throw new Error(`Subtask ${subtaskId} not found in local state`);

        const body = { title: subtask.title, completed: !subtask.completed };
        return this.http.put<SubtaskApiResponse>(
            `${this.API}/${taskId}/subtasks/${subtaskId}`, body
        ).pipe(
            tap(raw => {
                const updated = toSubtask(raw);
                this._tasks.update(ts =>
                    ts.map(t =>
                        t.id === taskId
                            ? { ...t, subtasks: t.subtasks.map(s => s.id === subtaskId ? updated : s) }
                            : t
                    )
                );
            }),
            map(raw => toSubtask(raw))
        );
    }

    deleteSubtask(taskId: number, subtaskId: number): Observable<void> {
        return this.http.delete<void>(`${this.API}/${taskId}/subtasks/${subtaskId}`).pipe(
            tap(() =>
                this._tasks.update(ts =>
                    ts.map(t =>
                        t.id === taskId
                            ? { ...t, subtasks: t.subtasks.filter(s => s.id !== subtaskId) }
                            : t
                    )
                )
            )
        );
    }
}

