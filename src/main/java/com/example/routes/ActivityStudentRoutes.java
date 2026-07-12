package com.example.routes;

import com.example.controllers.ActivityStudentController;

import io.javalin.Javalin;

public class ActivityStudentRoutes {

    private final ActivityStudentController controller;


    public ActivityStudentRoutes(
            ActivityStudentController controller) {

        this.controller = controller;
    }


    public void register(Javalin app) {


        app.post(
                "/students/{studentId}/activities/{activityId}/start",
                controller::start
        );


        app.get(
                "/students/{studentId}/activities/{activityId}",
                controller::findAttempt
        );
    }
}