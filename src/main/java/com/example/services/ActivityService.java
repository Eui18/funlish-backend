package com.example.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.dtos.activity.ActivityResponseDto;
import com.example.dtos.activity.CreateActivityDto;
import com.example.dtos.activity.UpdateActivityDto;
import com.example.models.activity.Activity;
import com.example.models.activity.ActivityStatus;
import com.example.models.activity.ActivityType;
import com.example.repository.activity.ActivityRepository;
import com.example.repository.topic.TopicRepository;
import com.example.utils.DtoValidator;

public class ActivityService {

    private final ActivityRepository activityRepository;
    private final TopicRepository topicRepository;

    public ActivityService(
            ActivityRepository activityRepository,
            TopicRepository topicRepository) {

        this.activityRepository = activityRepository;
        this.topicRepository = topicRepository;
    }


    // Crear actividad
    public void create(CreateActivityDto dto, String teacherId, String topicId) {

        DtoValidator.validate(dto);

        // Verificar que exista el tema
        if (topicRepository.findById(topicId).isEmpty()) {
            throw new IllegalArgumentException("El tema no existe.");
        }

        // Evitar títulos repetidos dentro del mismo tema
        if (activityRepository.existsByTitle(
                topicId,
                dto.getTitle())) {

            throw new IllegalArgumentException(
                    "Ya existe una actividad con ese título.");
        }

        Activity activity = new Activity(
                UUID.randomUUID().toString(),
                topicId,
                teacherId,
                dto.getTitle(),
                dto.getDescription(),
                parseType(dto.getType()),
                dto.getDurationMinutes(),
                dto.getScorePerQuestion(),
                ActivityStatus.DRAFT,
                LocalDateTime.now()
        );

        activityRepository.create(activity);
    }



    // Actualizar
    public void update(String id, UpdateActivityDto dto) {

        DtoValidator.validate(dto);


        Activity activity = activityRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Actividad no encontrada."
                        ));

        if (dto.getTitle() != null && !dto.getTitle().equalsIgnoreCase(activity.getTitle()) && activityRepository.existsByTitle(
                activity.getTopicId(),
                dto.getTitle())) {

            throw new IllegalArgumentException(
                    "Ya existe una actividad con ese título.");
        }

        if(activity.getStatus() != ActivityStatus.DRAFT){

            throw new IllegalArgumentException(
                "Solo se pueden modificar actividades en borrador."
            );
        }

        if (dto.getTitle() != null) {
            activity.setTitle(dto.getTitle());
        }

        if (dto.getDescription() != null) {
            activity.setDescription(dto.getDescription());
        }

        if (dto.getType() != null) {

            activity.setType(
                    parseType(dto.getType())
            );
        }

        if (dto.getDurationMinutes() != null) {

            activity.setDurationMinutes(
                    dto.getDurationMinutes()
            );
        }

        if (dto.getScorePerQuestion() != null) {

            activity.setScorePerQuestion(
                    dto.getScorePerQuestion()
            );
        }
        activityRepository.update(activity);
    }



    // Publicar
    public void publish(String id) {

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Actividad no encontrada."
                        ));

        if (activity.getStatus() == ActivityStatus.PUBLISHED) {

            throw new IllegalArgumentException(
                    "La actividad ya fue publicada."
            );
        }
        activityRepository.publish(id);
    }



    // Eliminar
    public void delete(String id) {

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Actividad no encontrada."
                        ));

        if (activityRepository.hasStudentAttempts(id)) {

            throw new IllegalArgumentException(
                    "No es posible eliminar una actividad con intentos registrados."
            );
        }
        activityRepository.delete(id);
    }



    // Buscar por id
    public ActivityResponseDto findById(String id) {

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Actividad no encontrada."
                        ));
        return map(activity);
    }



    // Listar actividades por tema
    public List<ActivityResponseDto> findAll(String topicId) {

        return activityRepository.findAll(topicId)
                .stream()
                .map(this::map)
                .toList();
    }


    // Validar tipo de actividad
    private ActivityType parseType(String type) {

        try {

            return ActivityType.valueOf(
                    type.toUpperCase()
            );

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Tipo de actividad no válido."
            );
        }
    }

    // Convertir Model -> DTO
    private ActivityResponseDto map(Activity activity) {

        return new ActivityResponseDto(
                activity.getId(),
                activity.getTopicId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getType().name(),
                activity.getDurationMinutes(),
                activity.getScorePerQuestion(),
                activity.getStatus().name()
        );
    }
}