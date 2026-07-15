package com.example.models.forum;

import java.time.LocalDateTime;

public class Comment {

    private String id;
    private String forumId;
    private String userId;
    private String comment;
    private LocalDateTime createdAt;

    public Comment() {
    }

    public Comment(
            String id,
            String forumId,
            String userId,
            String comment,
            LocalDateTime createdAt) {

        this.id = id;
        this.forumId = forumId;
        this.userId = userId;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getForumId() {
        return forumId;
    }

    public String getUserId() {
        return userId;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}