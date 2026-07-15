package com.example.dtos.activityStudent;

import java.util.List;

public class AnswerReviewDto {

    private Integer number;
    private String statement;
    private List<String> options;
    private String correctAnswer;
    private String studentAnswer;
    private boolean correct;

    public AnswerReviewDto(
            Integer number,
            String statement,
            List<String> options,
            String correctAnswer,
            String studentAnswer,
            boolean correct) {

        this.number = number;
        this.statement = statement;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.studentAnswer = studentAnswer;
        this.correct = correct;
    }

    public Integer getNumber() {
        return number;
    }

    public String getStatement() {
        return statement;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }
}