package com.killeen.taskflow.db.mapper.custom;

import java.time.OffsetDateTime;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.killeen.taskflow.db.model.generated.TaskListTemplateDb;

@Mapper
public interface TaskListTemplateDbCustomMapper {

    /**
     * Attempts to exclusively claim a single template row for generation.
     *
     * <p>Uses {@code FOR UPDATE SKIP LOCKED} so that a concurrent caller that has already
     * locked the same row will receive {@code null} rather than blocking. The
     * {@code nextGenerate} equality check acts as an optimistic-lock guard: if a preceding
     * instance has already committed a new {@code next_generate} value, this query returns
     * nothing, preventing duplicate generation.
     *
     * <p>Must be called inside an active transaction so the row lock is held for the
     * duration of the generation work.
     */
    TaskListTemplateDb findByIdAndNextGenerateForUpdateSkipLocked(
            @Param("id") Long id,
            @Param("nextGenerate") OffsetDateTime nextGenerate);
}
