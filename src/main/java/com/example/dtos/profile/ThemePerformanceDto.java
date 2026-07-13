package com.example.dtos.profile;

public class ThemePerformanceDto {

    private String theme;
    private double percentage;

    public ThemePerformanceDto() {
    }

    public ThemePerformanceDto(
            String theme,
            double percentage
    ) {
        this.theme = theme;
        this.percentage = percentage;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}