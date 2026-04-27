import { inject, Injectable, signal } from '@angular/core';
import { map, Observable, tap } from 'rxjs';
import { TaskList } from '../interfaces/task.interface';
import { toTaskList } from '../mappers/task-list.mapper';
import { TaskListService } from './task-list.service';

@Injectable({ providedIn: 'root' })
export class TaskListStateService {

    private readonly taskListService = inject(TaskListService);

    private readonly _lists = signal<TaskList[]>([]);
    private readonly _loading = signal(false);

    readonly lists = this._lists.asReadonly();
    readonly loading = this._loading.asReadonly();

    loadAll(): Observable<void> {
        this._loading.set(true);
        return this.taskListService.getAll().pipe(
            tap(raw => {
                this._lists.set(raw.map(toTaskList));
                this._loading.set(false);
            }),
            map(() => void 0)
        );
    }

    addList(body: { name: string; color?: string }): void {
        this.taskListService.createList(body).pipe(
            tap(raw => this._lists.update(ls => [...ls, toTaskList(raw)]))
        ).subscribe({ error: console.error });
    }

    updateList(id: number, body: { name: string; color: string }): void {
        this.taskListService.updateList(id, body).pipe(
            tap(raw => {
                const updated = toTaskList(raw);
                this._lists.update(ls => ls.map(l => l.id === id ? updated : l));
            })
        ).subscribe({ error: console.error });
    }

    deleteList(id: number): void {
        this.taskListService.deleteList(id).pipe(
            tap(() => this._lists.update(ls => ls.filter(l => l.id !== id)))
        ).subscribe({ error: console.error });
    }
}
