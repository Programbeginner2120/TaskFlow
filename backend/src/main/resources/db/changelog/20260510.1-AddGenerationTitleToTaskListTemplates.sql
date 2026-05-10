--liquibase formatted sql
--changeset mattk:20260510.1-AddGenerationTitleToTaskListTemplates.sql endDelimiter:go

ALTER TABLE task_list_templates
    ADD COLUMN generation_title TEXT;

-- generation_title is AES-256-GCM encrypted and base64-encoded at the application layer.
