--liquibase formatted sql
--changeset mattk:20260513.1-AddSelectorToRefreshTokenTable.sql endDelimiter:go

ALTER TABLE refresh_tokens
ADD COLUMN selector VARCHAR(32) NOT NULL DEFAULT '';

-- remove the default so future inserts must provide a selector
ALTER TABLE refresh_tokens
ALTER COLUMN selector DROP DEFAULT;

ALTER TABLE refresh_tokens
ADD CONSTRAINT uq_refresh_tokens_selector UNIQUE (selector);

CREATE INDEX idx_refresh_tokens_selector ON refresh_tokens(selector);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
