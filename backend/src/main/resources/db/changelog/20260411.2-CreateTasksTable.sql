--liquibase formatted sql
--changeset mattk:20260411.2-CreateTasksTable endDelimiter:go

CREATE TABLE tasks (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT    NOT NULL,
    list_id     BIGINT,
    title       TEXT      NOT NULL,
    notes       TEXT,
    completed   BOOLEAN   NOT NULL DEFAULT FALSE,
    due_date    DATE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- title and notes are AES-256-GCM encrypted and base64-encoded at the application layer.

ALTER TABLE tasks
    ADD CONSTRAINT fk_tasks_user_id
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE tasks
    ADD CONSTRAINT fk_tasks_list_id
    FOREIGN KEY (list_id) REFERENCES task_lists(id) ON DELETE CASCADE;
