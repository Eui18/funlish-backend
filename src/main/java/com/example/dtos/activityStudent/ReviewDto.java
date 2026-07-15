package com.example.dtos.activityStudent;

import java.math.BigDecimal;
import java.util.List;

public class ReviewDto {

    private BigDecimal score;
    private BigDecimal bonusPoints;
    private BigDecimal totalScore;
    private Integer timeSpent;
    private double percentage;
    private List<AnswerReviewDto> answers;

    public ReviewDto(
            BigDecimal score,
            BigDecimal bonusPoints,
            BigDecimal totalScore,
            Integer timeSpent,
            double percentage,
            List<AnswerReviewDto> answers) {

        this.score = score;
        this.bonusPoints = bonusPoints;
        this.totalScore = totalScore;
        this.timeSpent = timeSpent;
        this.percentage = percentage;
        this.answers = answers;
    }

    public BigDecimal getScore() {
        return score;
    }

    public BigDecimal getBonusPoints() {
        return bonusPoints;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public double getPercentage() {
        return percentage;
    }

    public List<AnswerReviewDto> getAnswers() {
        return answers;
    }
}