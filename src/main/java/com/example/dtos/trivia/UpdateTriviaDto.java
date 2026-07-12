package com.example.dtos.trivia;

import java.util.List;

public class UpdateTriviaDto {

    private String statement;
    private List<String> options;

    public UpdateTriviaDto() {
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