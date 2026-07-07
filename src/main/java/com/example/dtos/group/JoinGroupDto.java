package com.example.dtos.group;

import jakarta.validation.constraints.NotBlank;

public class JoinGroupDto {

    @NotBlank(message = "El código del grupo es obligatorio")
    private String accessCode;


    public JoinGroupDto() {
    }


    public String getAccessCode() {
        return accessCode;
    }


    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}