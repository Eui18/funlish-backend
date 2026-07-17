package com.example.controllers;

import com.example.services.ActivityStudentService;
import io.javalin.http.Context;

public class ActivityStudentController {

    private final ActivityStudentService service;

    public ActivityStudentController(ActivityStudentService service) {
        this.service = service;
    }


    public void start(Context context) {

        String studentId = context.attribute("userId");
        String activityId = context.pathParam("activityId");

        context.json(service.startActivity(studentId, activityId));
    }


    public void findAttempt(Context context) {

        String studentId = context.attribute("userId");
        String activityId = context.pathParam("activityId");

        context.json(service.findAttempt(studentId, activityId));
    }
}