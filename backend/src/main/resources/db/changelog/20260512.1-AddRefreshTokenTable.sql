--liquibase formatted sql
--changeset mattk:20260512.1-AddRefreshTokenTable.sql endDelimiter:go

CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(64) NOT NULL UNIQUE,
    created_at TIMESTAMPZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMPZ NOT NULL,
    used_at TIMESTAMPZ
);

ALTER TABLE refresh_tokens
ADD CONSTRAINT fk_refresh_tokens_user_id
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);