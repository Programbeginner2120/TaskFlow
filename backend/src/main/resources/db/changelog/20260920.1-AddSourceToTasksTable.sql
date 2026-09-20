--liquibase formatted sql
--changeset mattk:20260920.1-AddSourceToTasksTable endDelimiter:go

CREATE TYPE task_source AS ENUM ('USER', 'SYSTEM');

ALTER TABLE tasks
    ADD COLUMN source task_source NOT NULL DEFAULT 'USER';

-- Index supports analytics queries that filter dashboard data by source per-user
CREATE INDEX idx_tasks_user_source ON tasks(user_id, source);
