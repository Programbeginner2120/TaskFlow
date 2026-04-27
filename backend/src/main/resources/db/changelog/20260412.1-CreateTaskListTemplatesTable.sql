--liquibase formatted sql
--changeset mattk:20260412.1-CreateTaskListTemplatesTable endDelimiter:go

CREATE TABLE task_list_templates (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT       NOT NULL,
    name            TEXT         NOT NULL,
    color           VARCHAR(7)   NOT NULL DEFAULT '#6366f1',
    rrule           TEXT         NOT NULL,
    timezone        TEXT         NOT NULL DEFAULT 'UTC',
    last_generated  TIMESTAMPTZ,
    next_generate   TIMESTAMPTZ  NOT NULL,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- name is AES-256-GCM encrypted and base64-encoded at the application layer.

ALTER TABLE task_list_templates
    ADD CONSTRAINT fk_task_list_templates_user_id
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- Scheduler polls this index to find templates that are due
CREATE INDEX idx_task_list_templates_next_generate ON task_list_templates(next_generate);
CREATE INDEX idx_task_list_templates_user_id ON task_list_templates(user_id);
