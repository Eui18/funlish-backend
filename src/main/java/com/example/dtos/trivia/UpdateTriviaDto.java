package com.example.dtos.trivia;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;


public class UpdateTriviaDto {

    private String statement;

    @Size(min = 4, max = 4, message = "Debe tener exactamente 4 opciones.")
    @Valid
    private List<OptionDto> options;

    public UpdateTriviaDto(){
    }

    public String getStatement() {
        return statement;
    }

    public List<OptionDto> getOptions() {
        return options;
    }
}