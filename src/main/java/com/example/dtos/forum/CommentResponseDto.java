package com.example.dtos.forum;

public class CommentResponseDto {

    private String id;
    private String userId;
    private String userName;
    private String comment;
    private String createdAt;

    public CommentResponseDto(
            String id,
            String userId,
            String userName,
            String comment,
            String createdAt) {

        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}