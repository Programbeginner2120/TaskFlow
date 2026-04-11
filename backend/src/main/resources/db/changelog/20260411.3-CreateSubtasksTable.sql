--liquibase formatted sql
--changeset mattk:20260411.3-CreateSubtasksTable endDelimiter:go

CREATE TABLE subtasks (
    id          BIGSERIAL PRIMARY KEY,
    task_id     BIGINT    NOT NULL,
    title       TEXT      NOT NULL,
    completed   BOOLEAN   NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- title is AES-256-GCM encrypted and base64-encoded at the application layer.

ALTER TABLE subtasks
    ADD CONSTRAINT fk_subtasks_task_id
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE;
