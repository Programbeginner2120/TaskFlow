import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TaskListApiResponse } from '../interfaces/task.interface';

@Injectable({ providedIn: 'root' })
export class TaskListService {

    private readonly http = inject(HttpClient);
    private readonly API = '/task-lists';

    getAll(): Observable<TaskListApiResponse[]> {
        return this.http.get<TaskListApiResponse[]>(this.API);
    }

    createList(body: { name: string; color?: string }): Observable<TaskListApiResponse> {
        return this.http.post<TaskListApiResponse>(this.API, body);
    }

    updateList(id: number, body: { name: string; color: string }): Observable<TaskListApiResponse> {
        return this.http.put<TaskListApiResponse>(`${this.API}/${id}`, body);
    }

    deleteList(id: number): Observable<void> {
        return this.http.delete<void>(`${this.API}/${id}`);
    }
}



