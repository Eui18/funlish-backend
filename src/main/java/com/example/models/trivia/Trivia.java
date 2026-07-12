package com.example.models.trivia;

public class Trivia {

    private String id;
    private String activityId;
    private int number;
    private String statement;
    private String hiddenWord;
    private String correctAnswer;

    public Trivia(
            String id,
            String activityId,
            int number,
            String statement,
            String hiddenWord,
            String correctAnswer) {

        this.id = id;
        this.activityId = activityId;
        this.number = number;
        this.statement = statement;
        this.hiddenWord = hiddenWord;
        this.correctAnswer = correctAnswer;
    }

    public String getId() {
        return id;
    }

    public String getActivityId() {
        return activityId;
    }

    public int getNumber() {
        return number;
    }

    public String getStatement() {
        return statement;
    }

    public String getHiddenWord() {
        return hiddenWord;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public void setHiddenWord(String hiddenWord) {
        this.hiddenWord = hiddenWord;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}