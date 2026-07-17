package com.example.routes;

import com.example.controllers.StudentController;
import io.javalin.Javalin;

public class StudentRoutes {

    private final StudentController studentController;

    public StudentRoutes(StudentController studentController) {
        this.studentController = studentController;
    }

    public void register(Javalin app) {

        app.post("/students/group", studentController::joinGroup);
        app.get("/students/group", studentController::findGroup);
        app.delete("/students/group", studentController::leaveGroup);
    }
}