package com.example.dtos.trivia;

import java.util.List;

import com.example.models.trivia.Option;

public class TriviaResponseDto {

    private String id;
    private Integer questionNumber;
    private String statement;
    private List<Option> options;

    public TriviaResponseDto(
            String id,
            Integer questionNumber,
            String statement,
            List<Option> options) {

        this.id = id;
        this.questionNumber = questionNumber;
        this.statement = statement;
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public String getStatement() {
        return statement;
    }

    public List<Option> getOptions() {
        return options;
    }
}