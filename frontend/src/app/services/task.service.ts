import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TaskApiResponse, SubtaskApiResponse, TaskRequestBody } from '../interfaces/task.interface';

@Injectable({ providedIn: 'root' })
export class TaskService {

    private readonly http = inject(HttpClient);
    private readonly API = '/tasks';

    getAll(): Observable<TaskApiResponse[]> {
        return this.http.get<TaskApiResponse[]>(this.API);
    }

    createTask(body: Pick<TaskRequestBody, 'title' | 'dueDate' | 'listId'>): Observable<TaskApiResponse> {
        return this.http.post<TaskApiResponse>(this.API, body);
    }

    updateTask(taskId: number, body: TaskRequestBody): Observable<TaskApiResponse> {
        return this.http.put<TaskApiResponse>(`${this.API}/${taskId}`, body);
    }

    deleteTask(taskId: number): Observable<void> {
        return this.http.delete<void>(`${this.API}/${taskId}`);
    }

    createSubtask(taskId: number, body: { title: string }): Observable<SubtaskApiResponse> {
        return this.http.post<SubtaskApiResponse>(`${this.API}/${taskId}/subtasks`, body);
    }

    updateSubtask(taskId: number, subtaskId: number, body: { title: string; completed: boolean }): Observable<SubtaskApiResponse> {
        return this.http.put<SubtaskApiResponse>(`${this.API}/${taskId}/subtasks/${subtaskId}`, body);
    }

    deleteSubtask(taskId: number, subtaskId: number): Observable<void> {
        return this.http.delete<void>(`${this.API}/${taskId}/subtasks/${subtaskId}`);
    }
}


