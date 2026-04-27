package com.killeen.taskflow.db.model.generated;

import java.time.OffsetDateTime;

public class TaskTemplateDb {
    private Long id;

    private Long listTemplateId;

    private String title;

    private String notes;

    private Integer dueDateOffset;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getListTemplateId() {
        return listTemplateId;
    }

    public void setListTemplateId(Long listTemplateId) {
        this.listTemplateId = listTemplateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }

    public Integer getDueDateOffset() {
        return dueDateOffset;
    }

    public void setDueDateOffset(Integer dueDateOffset) {
        this.dueDateOffset = dueDateOffset;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}