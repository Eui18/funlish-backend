package com.example.dtos.scramble;

public class ScrambleResponseDto {

    private String id;
    private Integer numberChallenge;
    private String correctContent;
    private String type;

    public ScrambleResponseDto(
            String id,
            Integer numberChallenge,
            String correctContent,
            String type
    ) {
        this.id = id;
        this.numberChallenge = numberChallenge;
        this.correctContent = correctContent;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public Integer getNumberChallenge() {
        return numberChallenge;
    }

    public String getCorrectContent() {
        return correctContent;
    }

    public String getType() {
        return type;
    }
}