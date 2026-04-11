import { Injectable, signal } from '@angular/core';
import { TaskList } from '../shared/interfaces/task.interface';

let nextListId = 3;

const MOCK_LISTS: TaskList[] = [
    { id: 1, userId: 1, name: 'Work', color: '#6366F1' },
    { id: 2, userId: 1, name: 'Personal', color: '#10B981' },
];

@Injectable({
    providedIn: 'root',
})
export class TaskListService {
    private readonly _lists = signal<TaskList[]>(MOCK_LISTS);

    readonly lists = this._lists.asReadonly();

    addList(name: string): void {
        const colors = ['#6366F1', '#10B981', '#F59E0B', '#EF4444', '#3B82F6', '#A855F7', '#F97316'];
        const color = colors[(nextListId - 1) % colors.length];
        const newList: TaskList = {
            id: nextListId++,
            userId: 1,
            name: name.trim(),
            color,
        };
        this._lists.update(lists => [...lists, newList]);
    }

    updateList(id: number, partial: Partial<Pick<TaskList, 'name' | 'color'>>): void {
        this._lists.update(lists =>
            lists.map(l => l.id === id ? { ...l, ...partial } : l)
        );
    }

    deleteList(id: number): void {
        this._lists.update(lists => lists.filter(l => l.id !== id));
    }
}
