package com.example.models.forum;

import java.time.LocalDateTime;

public class Forum {

    private String id;
    private String groupId;
    private String teacherId;
    private String title;
    private String description;
    private String url;
    private LocalDateTime createdAt;

    public Forum() {
    }

    public Forum(
            String id,
            String groupId,
            String teacherId,
            String title,
            String description,
            String url,
            LocalDateTime createdAt) {

        this.id = id;
        this.groupId = groupId;
        this.teacherId = teacherId;
        this.title = title;
        this.description = description;
        this.url = url;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}