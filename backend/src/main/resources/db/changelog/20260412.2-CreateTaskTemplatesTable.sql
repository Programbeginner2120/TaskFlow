--liquibase formatted sql
--changeset mattk:20260412.2-CreateTaskTemplatesTable endDelimiter:go

CREATE TABLE task_templates (
    id                  BIGSERIAL PRIMARY KEY,
    list_template_id    BIGINT  NOT NULL,
    title               TEXT    NOT NULL,
    notes               TEXT,
    due_date_offset     INT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- title and notes are AES-256-GCM encrypted and base64-encoded at the application layer.

ALTER TABLE task_templates
    ADD CONSTRAINT fk_task_templates_list_template_id
    FOREIGN KEY (list_template_id) REFERENCES task_list_templates(id) ON DELETE CASCADE;
