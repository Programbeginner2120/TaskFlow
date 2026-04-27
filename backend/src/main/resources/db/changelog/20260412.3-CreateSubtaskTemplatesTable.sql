--liquibase formatted sql
--changeset mattk:20260412.3-CreateSubtaskTemplatesTable endDelimiter:go

CREATE TABLE subtask_templates (
    id                  BIGSERIAL PRIMARY KEY,
    task_template_id    BIGINT  NOT NULL,
    title               TEXT    NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- title is AES-256-GCM encrypted and base64-encoded at the application layer.

ALTER TABLE subtask_templates
    ADD CONSTRAINT fk_subtask_templates_task_template_id
    FOREIGN KEY (task_template_id) REFERENCES task_templates(id) ON DELETE CASCADE;
