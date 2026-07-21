package com.example.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.dtos.activityStudent.ActivityStudentResponseDto;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ResourceAlreadyExistsException;
import com.example.exceptions.ValidationException;
import com.example.models.activity.Activity;
import com.example.models.activity.ActivityStatus;
import com.example.models.activityStudent.ActivityStudent;
import com.example.models.activityStudent.ActivityStudentStatus;
import com.example.models.user.Role;
import com.example.models.user.User;
import com.example.repository.activity.ActivityRepository;
import com.example.repository.activityStudent.ActivityStudentRepository;
import com.example.repository.auth.AuthRepository;

public class ActivityStudentService {

    private final ActivityStudentRepository repository;
    private final ActivityRepository activityRepository;
    private final AuthRepository authRepository;

    public ActivityStudentService(
            ActivityStudentRepository repository,
            ActivityRepository activityRepository,
            AuthRepository authRepository) {

        this.repository = repository;
        this.activityRepository = activityRepository;
        this.authRepository = authRepository;
    }


    public ActivityStudentResponseDto startActivity(
            String studentId,
            String activityId) {

        User student = authRepository.findById(studentId)
                .orElseThrow(() ->
                        new NotFoundException("El alumno no existe.")
                );

        if (student.getRole() != Role.STUDENT) {
            throw new ValidationException(
                    List.of("El usuario no es alumno.")
            );
        }

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new NotFoundException("La actividad no existe.")
                );

        if (activity.getStatus() != ActivityStatus.PUBLISHED) {
            throw new ValidationException(
                    List.of("La actividad no está disponible.")
            );
        }

        var existing = repository.findByStudentAndActivity(
                studentId,
                activityId
        );

        if (existing.isPresent()) {
            return toResponseDto(existing.get());
        }


        ActivityStudent attempt = new ActivityStudent(
                UUID.randomUUID().toString(),
                activityId,
                studentId,
                ActivityStudentStatus.EN_PROGRESO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                null,
                LocalDateTime.now(),
                null,
                1
        );

        try {
            repository.create(attempt);
        } catch (ResourceAlreadyExistsException e) {
            // Carrera: otra petición concurrente (p. ej. el doble montaje de
            // React StrictMode) creó el intento entre el findByStudentAndActivity
            // de arriba y este INSERT. Se recupera y se devuelve ese intento,
            // por lo que start es idempotente y nunca lanza 500 por duplicado.
            return repository.findByStudentAndActivity(studentId, activityId)
                    .map(this::toResponseDto)
                    .orElseThrow(() -> new NotFoundException(
                            "No se pudo recuperar el intento existente."));
        }

        return toResponseDto(attempt);
    }


    public ActivityStudentResponseDto findAttempt(
            String studentId,
            String activityId) {

        ActivityStudent attempt =
                repository.findByStudentAndActivity(
                        studentId,
                        activityId
                )
                .orElseThrow(() ->
                        new NotFoundException(
                                "El alumno no tiene iniciado esta actividad."
                        )
                );

        return toResponseDto(attempt);
    }


    private ActivityStudentResponseDto toResponseDto(
            ActivityStudent activityStudent) {

      return new ActivityStudentResponseDto(
        activityStudent.getId(),
        activityStudent.getActivityId(),
        activityStudent.getStudentId(),
        activityStudent.getStatus().name(),
        activityStudent.getScore(),
        activityStudent.getBonusPoints(),
        activityStudent.getTimeSpent(),
        activityStudent.getStartDate() != null ? activityStudent.getStartDate().toString() : null,
        activityStudent.getDeliveryDate() != null ? activityStudent.getDeliveryDate().toString() : null
        );
    }
}