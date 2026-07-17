package com.example.dtos.activity;

import jakarta.validation.constraints.NotBlank;

public class DuplicateActivityDto {

    @NotBlank(message = "El grupo destino es obligatorio.")
    private String targetGroupId;

    public DuplicateActivityDto() {
    }

    public String getTargetGroupId() {
        return targetGroupId;
    }

    public void setTargetGroupId(String targetGroupId) {
        this.targetGroupId = targetGroupId;
    }
}