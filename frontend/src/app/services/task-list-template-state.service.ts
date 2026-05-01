import { inject, Injectable, signal } from "@angular/core";
import { CreateTaskListTemplateRequest, TaskListTemplate, UpdateTaskListTemplateRequest } from "../interfaces/task.interface";
import { TaskListTemplateService } from "./task-list-template.service";
import { toTaskListTemplate } from "../mappers/task-list-template.mapper";
import { map, Observable, tap } from "rxjs";

@Injectable({ providedIn: 'root' })
export class TaskListTemplateStateService {

    private readonly taskListTemplateService = inject(TaskListTemplateService);

    private readonly _templates = signal<TaskListTemplate[]>([]);
    private readonly _loading = signal<boolean>(false);

    readonly templates = this._templates.asReadonly();
    readonly loading = this._loading.asReadonly();

    loadAll(): Observable<void> {
        this._loading.set(true);
        return this.taskListTemplateService.getAllTemplates().pipe(
            tap(raw => {
                this._templates.set(raw.map(toTaskListTemplate));
                this._loading.set(false);
            }),
            map(() => void 0)
        );
    }

    addTemplate(body: CreateTaskListTemplateRequest): void {
        this.taskListTemplateService.createTemplate(body).pipe(
            tap(raw => this._templates.update(t => [...t, toTaskListTemplate(raw)]))
        ).subscribe({ error: console.error });
    }

    updateTemplate(id: number, body: UpdateTaskListTemplateRequest): void {
        this.taskListTemplateService.updateTemplate(id, body).pipe(
            tap(raw => {
                const updated = toTaskListTemplate(raw);
                this._templates.update(tmpl => tmpl.map(t => t.id === id ? updated : t));
            })
        ).subscribe({ error: console.error });
    }

    deleteList(id: number): void {
        this.taskListTemplateService.deleteTemplate(id).pipe(
            tap(() => this._templates.update(tmpl => tmpl.filter(t => t.id !== id)))
        ).subscribe({ error: console.error });
    }
}