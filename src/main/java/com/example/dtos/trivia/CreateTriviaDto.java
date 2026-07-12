package com.example.dtos.trivia;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTriviaDto {

    @NotBlank(message = "El enunciado es obligatorio.")
    private String statement;

    @NotNull(message = "Las opciones son obligatorias.")
    @Size(min = 4, max = 4, message = "Debe tener exactamente 4 opciones.")
    @Valid
    private List<OptionDto> options;

    public CreateTriviaDto(){
    }

    public String getStatement(){
        return statement;
    }

    public List<OptionDto> getOptions(){
        return options;
    }

}