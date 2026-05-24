--liquibase formatted sql
--changeset mattk:20260524.1-RefactorTaskCompleted.sql endDelimiter:go

ALTER TABLE tasks
ADD COLUMN completed_at TIMESTAMPTZ;

ALTER TABLE tasks
DROP COLUMN completed;