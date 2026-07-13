package com.example.routes;

import com.example.controllers.ProfileController;

import io.javalin.Javalin;

public class ProfileRoutes {

    private final ProfileController controller;

    public ProfileRoutes(ProfileController controller) {
        this.controller = controller;
    }


    public void register(Javalin app) {

        // Perfil completo del alumno
        app.get(
            "/profiles/student/{studentId}",
            controller::getStudentProfile
        );


        // Dashboard completo del docente
        app.get(
            "/profiles/teacher/{teacherId}",
            controller::getTeacherDashboard
        );
    }
}