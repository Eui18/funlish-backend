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


        app.post("/activities/{activityId}/start", controller::start);


        app.get("/activities/{activityId}/attempt", controller::findAttempt);
    }
}