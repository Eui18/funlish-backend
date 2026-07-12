package com.example.dtos.trivia;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CreateTriviaDto {

    @NotBlank(message = "El enunciado es obligatorio.")
    private String statement;

    @NotEmpty(message = "Debe agregar las 4 opciones.")
    @Size(min = 4, max = 4, message = "La trivia debe tener exactamente 4 opciones.")
    private List<String> options;

    public CreateTriviaDto() {
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}