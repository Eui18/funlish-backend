package com.example.controllers;

import com.example.dtos.MessageResponseDto;
import com.example.dtos.group.JoinGroupDto;
import com.example.services.StudentService;
import com.example.utils.DtoValidator;

import io.javalin.http.Context;

public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    public void joinGroup(Context context) {

        String studentId = context.pathParam("studentId");
        JoinGroupDto dto = context.bodyAsClass(JoinGroupDto.class);
        DtoValidator.validate(dto);
        service.joinGroup(studentId, dto);
        context.status(200).json(new MessageResponseDto("Alumno unido correctamente al grupo"));
    }

    public void leaveGroup(Context context) {

        String studentId = context.pathParam("studentId");
        service.leaveGroup(studentId);
        context.status(200).json(new MessageResponseDto("Alumno retirado correctamente del grupo"));
    }

    public void findGroup(Context context) {

        String studentId = context.pathParam("studentId");
        var response = service.findGroup(studentId);
        context.status(200).json(response);
    }
}