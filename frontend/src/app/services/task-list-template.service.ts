import { inject, Injectable } from "@angular/core";
import { CreateTaskListTemplateRequest, TaskListTemplate, TaskListTemplateApiResponse, UpdateTaskListTemplateRequest } from "../interfaces/task.interface";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({providedIn: 'root'})
export class TaskListTemplateService {

    private readonly http = inject(HttpClient);

    private readonly API_URL = '/task-list-templates';

    getAllTemplates(): Observable<TaskListTemplateApiResponse[]> {
        return this.http.get<TaskListTemplateApiResponse[]>(this.API_URL);
    }

    createTemplate(request: CreateTaskListTemplateRequest): Observable<TaskListTemplateApiResponse> {
        return this.http.post<TaskListTemplateApiResponse>(this.API_URL, request);
    }

    updateTemplate(id: number, request: UpdateTaskListTemplateRequest): Observable<TaskListTemplateApiResponse> {
        return this.http.put<TaskListTemplateApiResponse>(`${this.API_URL}/${id}`, request);
    }

    deleteTemplate(id: number): Observable<void> {
        return this.http.delete<void>(`${this.API_URL}/${id}`);
    }
}