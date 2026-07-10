package com.example.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.example.dtos.activity.ActivityResponseDto;
import com.example.dtos.activity.CreateActivityDto;
import com.example.dtos.activity.UpdateActivityDto;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ResourceAlreadyExistsException;
import com.example.exceptions.ValidationException;
import com.example.models.activity.Activity;
import com.example.models.activity.ActivityType;
import com.example.repository.activity.ActivityRepository;
import com.example.repository.topic.TopicRepository;

public class ActivityService {

    private final ActivityRepository activityRepository;
    private final TopicRepository topicRepository;

    public ActivityService(ActivityRepository activityRepository, TopicRepository topicRepository) {
        this.activityRepository = activityRepository;
        this.topicRepository = topicRepository;
    }


    public ActivityResponseDto create(String topicId, String teacherId, CreateActivityDto dto) {

        if (topicRepository.findById(topicId).isEmpty()) {
            throw new NotFoundException("El tema especificado no fue encontrado.");
        }

        if (activityRepository.existsByTitle(topicId, dto.getTitle())) {
            throw new ResourceAlreadyExistsException("Ya existe una actividad con este título en este tema.");
        }

        LocalDate start = LocalDate.parse(dto.getStartDate());
        LocalDate end = LocalDate.parse(dto.getEndDate());
        LocalTime startTime = LocalTime.parse(dto.getStartTime());
        LocalTime endTime = LocalTime.parse(dto.getEndTime());

        validateDates(start, end, startTime, endTime);

        ActivityType type;
        try {
            type = ActivityType.valueOf(dto.getType().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ValidationException(List.of("Tipo de actividad no válido. Valores permitidos: TRIVIA, SCRAMBLE."));
        }

        Activity activity = new Activity(
                UUID.randomUUID().toString(),
                topicId,
                teacherId,
                dto.getTitle(),
                dto.getDescription(),
                type,
                dto.getMaxScore(),
                dto.getDurationMinutes(),
                start,
                end,
                startTime,
                endTime
        );

        activityRepository.create(activity);

        return toResponseDto(activity);
    }


    public ActivityResponseDto update(String id, UpdateActivityDto dto) {

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("La actividad no fue encontrada."));

        if (activityRepository.hasStudentAttempts(id)) {
            throw new ValidationException(List.of("La actividad no puede modificarse porque los alumnos ya comenzaron a realizarla."));
        }

        if (dto.getTitle() != null) {
            if (!activity.getTitle().equals(dto.getTitle())
                    && activityRepository.existsByTitle(activity.getTopicId(), dto.getTitle(), id)) {
                throw new ResourceAlreadyExistsException("Ya existe una actividad con ese título.");
            }
            activity.setTitle(dto.getTitle());
        }

        if (dto.getDescription() != null) {
            activity.setDescription(dto.getDescription());
        }

        if (dto.getType() != null) {
            try {
                activity.setType(ActivityType.valueOf(dto.getType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ValidationException(List.of("Tipo de actividad no válido. Use TRIVIA o SCRAMBLE."));
            }
        }

        if (dto.getMaxScore() != null) {
            activity.setMaxScore(dto.getMaxScore());
        }

        if (dto.getDurationMinutes() != null) {
            activity.setDurationMinutes(dto.getDurationMinutes());
        }

        if (dto.getStartDate() != null) {
            activity.setStartDate(LocalDate.parse(dto.getStartDate()));
        }

        if (dto.getEndDate() != null) {
            activity.setEndDate(LocalDate.parse(dto.getEndDate()));
        }

        if (dto.getStartTime() != null) {
            activity.setStartTime(LocalTime.parse(dto.getStartTime()));
        }

        if (dto.getEndTime() != null) {
            activity.setEndTime(LocalTime.parse(dto.getEndTime()));
        }

        validateDates(
                activity.getStartDate(),
                activity.getEndDate(),
                activity.getStartTime(),
                activity.getEndTime()
        );

        activityRepository.update(activity);

        return toResponseDto(activity);
    }


    public List<ActivityResponseDto> findAll(String topicId) {

        if (topicRepository.findById(topicId).isEmpty()) {
            throw new NotFoundException("El tema especificado no fue encontrado.");
        }

        return activityRepository.findAll(topicId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }


    public ActivityResponseDto findById(String id) {

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("La actividad no fue encontrada."));

        return toResponseDto(activity);
    }


    public void delete(String id) {

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("La actividad no fue encontrada."));

        if (activityRepository.hasStudentAttempts(id)) {
            throw new ValidationException(List.of("La actividad no puede eliminarse porque los alumnos ya han iniciado intentos."));
        }

        activityRepository.delete(activity.getId());
    }


    private void validateDates(LocalDate start, LocalDate end, LocalTime startHour, LocalTime endHour) {

        if (end.isBefore(start)) {
            throw new ValidationException(List.of("La fecha de finalización no puede ser anterior a la fecha de inicio."));
        }

        if (start.equals(end) && endHour.isBefore(startHour)) {
            throw new ValidationException(List.of("La hora de finalización no puede ser anterior a la hora de inicio."));
        }
    }


    private ActivityResponseDto toResponseDto(Activity activity) {

        return new ActivityResponseDto(
                activity.getId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getType().name(),
                activity.getMaxScore(),
                activity.getDurationMinutes(),
                activity.getStartDate().toString(),
                activity.getEndDate().toString(),
                activity.getStartTime().toString(),
                activity.getEndTime().toString()
        );
    }
}