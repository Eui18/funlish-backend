package com.example.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.dtos.scramble.CreateScrambleDto;
import com.example.dtos.scramble.ScrambleResponseDto;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ResourceAlreadyExistsException;
import com.example.exceptions.ValidationException;
import com.example.models.activity.Activity;
import com.example.models.activity.ActivityType;
import com.example.models.activity.ActivityStatus;
import com.example.models.scramble.Scramble;
import com.example.models.scramble.ScrambleType;
import com.example.models.user.Role;
import com.example.models.user.User;
import com.example.repository.activity.ActivityRepository;
import com.example.repository.auth.AuthRepository;
import com.example.repository.scramble.ScrambleRepository;

public class ScrambleService {

    private final ScrambleRepository repository;
    private final ActivityRepository activityRepository;
    private final AuthRepository authRepository;

    public ScrambleService(
            ScrambleRepository repository,
            ActivityRepository activityRepository,
            AuthRepository authRepository
    ) {
        this.repository = repository;
        this.activityRepository = activityRepository;
        this.authRepository = authRepository;
    }


    public ScrambleResponseDto create(
            String activityId,
            CreateScrambleDto dto,
            String teacherId
    ) {

        User teacher = authRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("El docente no existe."));

        if (teacher.getRole() != Role.TEACHER) {
            throw new ValidationException(List.of("Solo un docente puede crear retos."));
        }

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new NotFoundException("Actividad no encontrada.")
                );

        if (activity.getType() != ActivityType.SCRAMBLE) {
            throw new ValidationException(
                    List.of("La actividad no pertenece al tipo SCRAMBLE.")
            );
        }

        if (activity.getStatus() == ActivityStatus.PUBLISHED) {
            throw new ValidationException(
                    List.of("No se pueden modificar actividades publicadas.")
            );
        }

        String content = dto.getCorrectContent().trim();

        validateContent(content);

        if (repository.existsByContent(activityId, content)) {
            throw new ResourceAlreadyExistsException(
                    "Este scramble ya existe en la actividad."
            );
        }

        ScrambleType type = detectType(content);

        Scramble scramble = new Scramble(
                UUID.randomUUID().toString(),
                activityId,
                repository.nextNumberChallenge(activityId),
                content,
                type
        );

        repository.create(scramble);

        return toResponseDto(scramble);
    }



    public List<ScrambleResponseDto> findAll(String activityId) {

        List<ScrambleResponseDto> response = new ArrayList<>();

        for (Scramble scramble : repository.findAll(activityId)) {
            response.add(toResponseDto(scramble));
        }

        return response;
    }


    public void delete(String id) {

        Scramble scramble = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Scramble no encontrado.")
                );

        Activity activity = activityRepository.findById(
                scramble.getActivityId()
        ).orElseThrow(() ->
                new NotFoundException("Actividad no encontrada.")
        );

        if (activity.getStatus() == ActivityStatus.PUBLISHED) {
            throw new ValidationException(
                    List.of("No se puede eliminar un scramble publicado.")
            );
        }

        boolean deleted = repository.delete(id);

        if (!deleted) {
            throw new NotFoundException(
                    "No se pudo eliminar el scramble."
            );
        }
    }

    private void validateContent(String content) {

        if(content == null || content.isBlank()){

                throw new ValidationException(
                        List.of(
                        "El contenido del scramble es obligatorio."
                        )
                );
        }


        if(content.length() > 250){

                throw new ValidationException(
                        List.of(
                        "El contenido no puede superar 250 caracteres."
                        )
                );
        }

     }



    private ScrambleType detectType(String content) {

        int words = content.split("\\s+").length;

        if (words == 1) {
            return ScrambleType.WORD;
        }

        return ScrambleType.SENTENCE;
    }


    private ScrambleResponseDto toResponseDto(
            Scramble scramble
    ) {

        return new ScrambleResponseDto(
                scramble.getId(),
                scramble.getNumberChallenge(),
                scramble.getCorrectContent(),
                scramble.getType().name()
        );
    }
}