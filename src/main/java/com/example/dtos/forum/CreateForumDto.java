package com.example.dtos.forum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateForumDto {

    @NotBlank(message = "El título es obligatorio.")
    @Size(min = 3, max = 150, message = "El título debe tener entre 3 y 150 caracteres.")
    private String title;

    private String description;

    @NotBlank(message = "El enlace es obligatorio.")
    @Size(max = 500, message = "El enlace no puede superar los 500 caracteres.")
    private String url;

    @NotBlank(message = "El docente es obligatorio.")
    private String teacherId;

    public CreateForumDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}