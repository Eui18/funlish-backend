package com.example.routes;

import com.example.controllers.AuthController;
import io.javalin.Javalin;

public class AuthRoutes {
    private final AuthController authController;

    public AuthRoutes(AuthController authController) {
        this.authController = authController;
    }

    public void register(Javalin app) {
        app.post("/auth/register", authController::register);
        app.post("/auth/access", authController::login);
    }
}