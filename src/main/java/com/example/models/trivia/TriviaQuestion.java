package com.example.models.trivia;

import java.util.List;

public class TriviaQuestion {

    private String id;
    private String activityId;
    private int questionNumber;
    private String statement;
    private List<Option> options;

    public TriviaQuestion(
            String id,
            String activityId,
            int questionNumber,
            String statement){

        this.id = id;
        this.activityId = activityId;
        this.questionNumber = questionNumber;
        this.statement = statement;
    }

    public String getId(){
        return id;
    }

    public String getActivityId(){
        return activityId;
    }

    public int getQuestionNumber(){
        return questionNumber;
    }

    public String getStatement(){
        return statement;
    }

    public List<Option> getOptions(){
        return options;
    }

    public void setOptions(List<Option> options){
        this.options = options;
    }
}