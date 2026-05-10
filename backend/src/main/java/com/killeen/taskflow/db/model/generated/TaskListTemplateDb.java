package com.killeen.taskflow.db.model.generated;

import java.time.OffsetDateTime;

public class TaskListTemplateDb {
    private Long id;

    private Long userId;

    private String name;

    private String color;

    private String rrule;

    private String timezone;

    private OffsetDateTime lastGenerated;

    private OffsetDateTime nextGenerate;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private String generationTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public String getRrule() {
        return rrule;
    }

    public void setRrule(String rrule) {
        this.rrule = rrule == null ? null : rrule.trim();
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone == null ? null : timezone.trim();
    }

    public OffsetDateTime getLastGenerated() {
        return lastGenerated;
    }

    public void setLastGenerated(OffsetDateTime lastGenerated) {
        this.lastGenerated = lastGenerated;
    }

    public OffsetDateTime getNextGenerate() {
        return nextGenerate;
    }

    public void setNextGenerate(OffsetDateTime nextGenerate) {
        this.nextGenerate = nextGenerate;
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

    public String getGenerationTitle() {
        return generationTitle;
    }

    public void setGenerationTitle(String generationTitle) {
        this.generationTitle = generationTitle == null ? null : generationTitle.trim();
    }
}