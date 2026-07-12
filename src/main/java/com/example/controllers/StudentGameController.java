package com.example.controllers;

import com.example.dtos.activityStudent.AnswerDto;
import com.example.services.StudentGameService;
import com.example.utils.DtoValidator;

import io.javalin.http.Context;

public class StudentGameController {

    private final StudentGameService service;

    public StudentGameController(StudentGameService service) {
        this.service = service;
    }


    public void getContent(Context context) {

        String activityStudentId = context.pathParam("activityStudentId");

        context.json(
                service.getCurrentContent(activityStudentId)
        );
    }


    public void answerTrivia(Context context) {

        String activityStudentId = context.pathParam("activityStudentId");

        AnswerDto dto = context.bodyAsClass(AnswerDto.class);

        DtoValidator.validate(dto);

        context.json(
                service.answerTrivia(activityStudentId, dto)
        );
    }


    public void answerScramble(Context context) {

        String activityStudentId = context.pathParam("activityStudentId");

        AnswerDto dto = context.bodyAsClass(AnswerDto.class);

        DtoValidator.validate(dto);

        context.json(
                service.answerScramble(activityStudentId, dto)
        );
    }
}