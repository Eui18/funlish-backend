package com.example.routes;

import com.example.controllers.StudentGameController;

import io.javalin.Javalin;

public class StudentGameRoutes {

    private final StudentGameController controller;

    public StudentGameRoutes(StudentGameController controller) {
        this.controller = controller;
    }


    public void register(Javalin app) {

        // Obtener pregunta o reto actual
        app.get(
                "/student-games/{activityStudentId}/content",
                controller::getContent
        );


        // Responder trivia
        app.post(
                "/student-games/{activityStudentId}/trivia",
                controller::answerTrivia
        );


        // Responder scramble
        app.post(
                "/student-games/{activityStudentId}/scramble",
                controller::answerScramble
        );
    }
}