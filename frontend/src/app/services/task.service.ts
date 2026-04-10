import { Injectable, signal } from '@angular/core';
import { Task, Subtask } from '../shared/interfaces/task.interface';

const TODAY = new Date(2026, 3, 10); // April 10, 2026

let nextTaskId = 4;
let nextSubtaskId = 4;

const MOCK_TASKS: Task[] = [
    {
        id: 1,
        userId: 1,
        title: 'Review Q2 roadmap presentation',
        completed: false,
        dueDate: new Date(TODAY),
        listId: 1,
        notes: 'Focus on the timeline and resource allocation slides.',
        subtasks: [
            { id: 1, taskId: 1, title: 'Check timeline accuracy', completed: true },
            { id: 2, taskId: 1, title: 'Verify budget numbers', completed: false },
            { id: 3, taskId: 1, title: 'Add team feedback', completed: false },
        ],
        createdAt: new Date(TODAY),
    },
    {
        id: 2,
        userId: 1,
        title: 'Fix login page bug',
        completed: false,
        dueDate: new Date(TODAY),
        listId: 1,
        notes: '',
        subtasks: [],
        createdAt: new Date(TODAY),
    },
    {
        id: 3,
        userId: 1,
        title: 'Morning jog – 5km',
        completed: true,
        dueDate: new Date(TODAY),
        listId: 2,
        notes: '',
        subtasks: [],
        createdAt: new Date(TODAY),
    },
];

@Injectable({
    providedIn: 'root',
})
export class TaskService {
    private readonly _tasks = signal<Task[]>(MOCK_TASKS);

    readonly tasks = this._tasks.asReadonly();

    addTask(title: string, dueDate?: Date | null, listId?: number): void {
        const newTask: Task = {
            id: nextTaskId++,
            userId: 1,
            title: title.trim(),
            completed: false,
            dueDate: dueDate ?? null,
            listId: listId ?? 1,
            notes: '',
            subtasks: [],
            createdAt: new Date(),
        };
        this._tasks.update(tasks => [...tasks, newTask]);
    }

    toggleCompletion(taskId: number): void {
        this._tasks.update(tasks =>
            tasks.map(t => t.id === taskId ? { ...t, completed: !t.completed } : t)
        );
    }

    updateTask(taskId: number, partial: Partial<Omit<Task, 'id' | 'userId' | 'createdAt' | 'subtasks'>>): void {
        this._tasks.update(tasks =>
            tasks.map(t => t.id === taskId ? { ...t, ...partial } : t)
        );
    }

    deleteTask(taskId: number): void {
        this._tasks.update(tasks => tasks.filter(t => t.id !== taskId));
    }

    addSubtask(taskId: number, title: string): void {
        const newSubtask: Subtask = {
            id: nextSubtaskId++,
            taskId,
            title: title.trim(),
            completed: false,
        };
        this._tasks.update(tasks =>
            tasks.map(t =>
                t.id === taskId ? { ...t, subtasks: [...t.subtasks, newSubtask] } : t
            )
        );
    }

    toggleSubtaskCompletion(taskId: number, subtaskId: number): void {
        this._tasks.update(tasks =>
            tasks.map(t =>
                t.id === taskId
                    ? {
                          ...t,
                          subtasks: t.subtasks.map(st =>
                              st.id === subtaskId ? { ...st, completed: !st.completed } : st
                          ),
                      }
                    : t
            )
        );
    }

    deleteSubtask(taskId: number, subtaskId: number): void {
        this._tasks.update(tasks =>
            tasks.map(t =>
                t.id === taskId
                    ? { ...t, subtasks: t.subtasks.filter(st => st.id !== subtaskId) }
                    : t
            )
        );
    }
}
