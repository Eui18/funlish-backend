package com.example.models.scramble;

public class Scramble {

    private String id;
    private String activityId;
    private Integer numberChallenge;
    private String correctContent;
    private ScrambleType type;

    public Scramble(
            String id,
            String activityId,
            Integer numberChallenge,
            String correctContent,
            ScrambleType type
    ) {
        this.id = id;
        this.activityId = activityId;
        this.numberChallenge = numberChallenge;
        this.correctContent = correctContent;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getActivityId() {
        return activityId;
    }

    public Integer getNumberChallenge() {
        return numberChallenge;
    }

    public String getCorrectContent() {
        return correctContent;
    }

    public ScrambleType getType() {
        return type;
    }

    public void setCorrectContent(String correctContent) {
        this.correctContent = correctContent;
    }

    public void setType(ScrambleType type) {
        this.type = type;
    }
}