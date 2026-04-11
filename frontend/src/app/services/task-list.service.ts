import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { map, Observable, tap } from 'rxjs';
import { TaskList } from '../shared/interfaces/task.interface';
import { TaskListApiResponse } from '../shared/interfaces/api-response.interface';
import { toTaskList } from './task-list.mapper';

@Injectable({ providedIn: 'root' })
export class TaskListService {

    private readonly http = inject(HttpClient);
    private readonly API = '/task-lists';

    private readonly _lists = signal<TaskList[]>([]);
    private readonly _loading = signal(false);

    readonly lists = this._lists.asReadonly();
    readonly loading = this._loading.asReadonly();

    loadAll(): Observable<void> {
        this._loading.set(true);
        return this.http.get<TaskListApiResponse[]>(this.API).pipe(
            tap(raw => {
                this._lists.set(raw.map(toTaskList));
                this._loading.set(false);
            }),
            map(() => void 0)
        );
    }

    addList(name: string, color?: string): Observable<TaskList> {
        return this.http.post<TaskListApiResponse>(this.API, { name, color }).pipe(
            tap(raw => this._lists.update(ls => [...ls, toTaskList(raw)])),
            map(raw => toTaskList(raw))
        );
    }

    updateList(id: number, partial: Partial<Pick<TaskList, 'name' | 'color'>>): Observable<TaskList> {
        const existing = this._lists().find(l => l.id === id);
        const body = { name: existing?.name, color: existing?.color, ...partial };
        return this.http.put<TaskListApiResponse>(`${this.API}/${id}`, body).pipe(
            tap(raw => {
                const updated = toTaskList(raw);
                this._lists.update(ls => ls.map(l => l.id === id ? updated : l));
            }),
            map(raw => toTaskList(raw))
        );
    }

    deleteList(id: number): Observable<void> {
        return this.http.delete<void>(`${this.API}/${id}`).pipe(
            tap(() => this._lists.update(ls => ls.filter(l => l.id !== id)))
        );
    }
}

