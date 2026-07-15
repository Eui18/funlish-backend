package com.example.dtos.forum;

import java.util.List;

public class ForumResponseDto {

    private String id;
    private String groupId;
    private String teacherId;
    private String title;
    private String description;
    private String url;
    private String createdAt;
    private List<CommentResponseDto> comments;

    public ForumResponseDto(
            String id,
            String groupId,
            String teacherId,
            String title,
            String description,
            String url,
            String createdAt,
            List<CommentResponseDto> comments) {

        this.id = id;
        this.groupId = groupId;
        this.teacherId = teacherId;
        this.title = title;
        this.description = description;
        this.url = url;
        this.createdAt = createdAt;
        this.comments = comments;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public List<CommentResponseDto> getComments() {
        return comments;
    }
}