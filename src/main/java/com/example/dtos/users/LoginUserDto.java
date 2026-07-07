package com.example.dtos.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginUserDto {

    @NotBlank(message = "La matrícula es obligatoria")
    @Size(min = 4, message = "La matrícula debe tener al menos 4 caracteres")
    private String tuition;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;


    public LoginUserDto() {
    }

    public LoginUserDto(String tuition, String password) {
        this.tuition = tuition;
        this.password = password;
    }

    public String getTuition() {
        return tuition;
    }

    public void setTuition(String tuition) {
        this.tuition = tuition;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}