--liquibase formatted sql
--changeset mattk:20260411.1-CreateTaskListsTable endDelimiter:go

CREATE TABLE task_lists (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT    NOT NULL,
    name        TEXT      NOT NULL,
    color       VARCHAR(7) NOT NULL DEFAULT '#6366f1',
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- name is AES-256-GCM encrypted and base64-encoded at the application layer.

ALTER TABLE task_lists
    ADD CONSTRAINT fk_task_lists_user_id
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
