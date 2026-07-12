package com.example.dtos.activityStudent;

import java.math.BigDecimal;

public class ResultDto {

    private boolean correct;
    private BigDecimal points;
    private boolean finished;
    private Integer nextNumber;

    public ResultDto(
            boolean correct,
            BigDecimal points,
            boolean finished,
            Integer nextNumber) {

        this.correct = correct;
        this.points = points;
        this.finished = finished;
        this.nextNumber = nextNumber;
    }

    public boolean isCorrect() {
        return correct;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public boolean isFinished() {
        return finished;
    }

    public Integer getNextNumber() {
        return nextNumber;
    }
}