import { TaskListTemplate, TaskListTemplateApiResponse, TaskTemplate, TaskTemplateApiResponse } from "../interfaces/task.interface";


export function toTaskTemplate(raw: TaskTemplateApiResponse): TaskTemplate {
    return {
        ...raw,
        createdAt: new Date(raw.createdAt),
        updatedAt: raw.updatedAt ? new Date(raw.updatedAt) : null,
    };
}

export function toTaskListTemplate(raw: TaskListTemplateApiResponse): TaskListTemplate {
    return {
        ...raw,
        lastGenerated: raw.lastGenerated ? new Date(raw.lastGenerated) : null,
        nextGenerate: raw.nextGenerate ? new Date(raw.nextGenerate) : null,
        createdAt: new Date(raw.createdAt),
        updatedAt: raw.updatedAt ? new Date(raw.updatedAt) : null,
        taskTemplates: raw.taskTemplates.map(taskTemplate => toTaskTemplate(taskTemplate))
    };
}