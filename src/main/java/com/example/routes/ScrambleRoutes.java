package com.example.routes;

import com.example.controllers.ScrambleController;

import io.javalin.Javalin;

public class ScrambleRoutes {

    private final ScrambleController controller;

    public ScrambleRoutes(
            ScrambleController controller
    ) {
        this.controller = controller;
    }

    public void register(Javalin app) {

        app.post("/activities/{activityId}/scramble",controller::create);
        app.get("/activities/{activityId}/scramble", controller::findAll );

        app.delete("/activities/scramble/{id}", controller::delete );
    }
}