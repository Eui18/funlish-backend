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
        app.get("/groups", groupController::findAll);
        app.get("/groups/{id}", groupController::findById);
        app.post("/groups", groupController::create);
        app.patch("/groups/{id}", groupController::update);
        app.delete("/groups/{id}", groupController::delete);

        app.get("/groups/{id}/students", groupController::findStudents);
        app.get("/groups/{id}/students/count", groupController::countStudents);

        // Perfil de un alumno del grupo, consultado por el docente dueño del grupo
        app.get("/groups/{groupId}/students/{studentId}/profile", groupController::getStudentProfile);

        // Desvincular a un alumno del grupo (acción del docente dueño del grupo)
        app.delete("/groups/{groupId}/students/{studentId}", groupController::removeStudent);
    }
}
