package com.example.routes;

import com.example.controllers.ActivityController;
import io.javalin.Javalin;

public class ActivityRoutes {
    private final ActivityController activityController;

    public ActivityRoutes(ActivityController activityController) {
        this.activityController = activityController;
    }

    public void register(Javalin app) {
        app.post("/topics/{topicId}/activities", activityController::create);
        app.get("/topics/{topicId}/activities", activityController::findAll);
        app.get("/activities/{id}", activityController::findById);
        app.patch("/activities/{id}", activityController::update);
        app.patch("/activities/{id}/publish", activityController::publish);
        app.post("/activities/{id}/duplicate", activityController::duplicate);
        app.delete("/activities/{id}", activityController::delete);
    }
}