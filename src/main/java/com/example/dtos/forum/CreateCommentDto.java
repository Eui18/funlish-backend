package com.example.dtos.forum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCommentDto {

    @NotBlank(message = "El comentario es obligatorio.")
    @Size(max = 1000, message = "El comentario no puede superar los 1000 caracteres.")
    private String comment;

    @NotBlank(message = "El usuario es obligatorio.")
    private String userId;

    public CreateCommentDto() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}