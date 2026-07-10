package com.example.routes;

import com.example.controllers.GroupController;

import io.javalin.Javalin;

public class GroupRoutes {

    private final GroupController groupController;


    public GroupRoutes(GroupController groupController) {
        this.groupController = groupController;
    }

    public void register(Javalin app) {

        //docente
        app.get("/groups/teacher/{teacherId}", groupController::findAll);
        app.get("/groups/{id}", groupController::findById);
        app.post("/groups", groupController::create);
        app.patch("/groups/{id}", groupController::update);
        app.delete("/groups/{id}", groupController::delete);

        app.get("/groups/{id}/students", groupController::findStudents);
        app.get("/groups/{id}/students/count", groupController::countStudents);
    }
}