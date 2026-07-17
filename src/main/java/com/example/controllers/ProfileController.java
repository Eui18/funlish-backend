package com.example.controllers;

import com.example.services.ProfileService;
import io.javalin.http.Context;

public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    // PERFIL ALUMNO
    public void getStudentProfile(Context context) {

        String studentId = context.attribute("userId");
        context.json(service.getStudentProfile(studentId)
        );
    }

    // DASHBOARD DOCENTE
    public void getTeacherDashboard(Context context) {

        String teacherId = context.attribute("userId");
        context.json(service.getTeacherDashboard(teacherId));
    }
}