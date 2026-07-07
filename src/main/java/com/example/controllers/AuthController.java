package com.example.controllers;

import com.example.dtos.users.CreateUserDto;
import com.example.dtos.users.LoginUserDto;
import com.example.services.AuthService;
import com.example.utils.DtoValidator;

import io.javalin.http.Context;

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public void register(Context context) {

        CreateUserDto dto = context.bodyAsClass(CreateUserDto.class);
        DtoValidator.validate(dto);

        var response = authService.registerUser(dto);

        context.status(201).json(response);
    }

    public void login(Context context) {

        LoginUserDto dto = context.bodyAsClass(LoginUserDto.class);
        DtoValidator.validate(dto);
        var response = authService.findUser(dto);

        context.status(200).json(response);
    }
}