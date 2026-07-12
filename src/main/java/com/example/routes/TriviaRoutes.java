package com.example.routes;

import com.example.controllers.TriviaController;

import io.javalin.Javalin;

public class TriviaRoutes {

    private final TriviaController triviaController;

    public TriviaRoutes(TriviaController triviaController) {
        this.triviaController = triviaController;
    }

    public void register(Javalin app) {

        app.post(
                "/activities/{activityId}/trivia",
                triviaController::create);

        app.get(
                "/activities/{activityId}/trivia",
                triviaController::findAll);

        app.delete(
                "/activities/trivia/{id}",
                triviaController::delete);
        app.put(
        "/activities/{activityId}/trivia/{id}",
        triviaController::update
);
    }
}