--liquibase formatted sql
--changeset mattk:20260509.1-CreatePositionOnTask endDelimiter:go

ALTER TABLE public.tasks
ADD COLUMN position BIGINT;