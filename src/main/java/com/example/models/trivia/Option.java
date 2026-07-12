package com.example.models.trivia;

public class Option {

    private String id;
    private String triviaId;
    private String text;
    private boolean correct;

    public Option(
            String id,
            String triviaId,
            String text,
            boolean correct){

        this.id = id;
        this.triviaId = triviaId;
        this.text = text;
        this.correct = correct;
    }

    public String getId(){
        return id;
    }

    public String getTriviaId(){
        return triviaId;
    }

    public String getText(){
        return text;
    }

    public boolean isCorrect(){
        return correct;
    }
}