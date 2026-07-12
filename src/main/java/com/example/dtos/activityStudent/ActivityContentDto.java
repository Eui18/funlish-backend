package com.example.dtos.activityStudent;

import java.util.List;

public class ActivityContentDto {

    private String activityId;
    private String type;
    private Integer number;
    private String statement;
    private List<String> options;

    public ActivityContentDto(
            String activityId,
            String type,
            Integer number,
            String statement,
            List<String> options) {

        this.activityId = activityId;
        this.type = type;
        this.number = number;
        this.statement = statement;
        this.options = options;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getType() {
        return type;
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
}