package com.example.services;

import java.util.List;
import java.util.UUID;

import com.example.dtos.trivia.CreateTriviaDto;
import com.example.dtos.trivia.TriviaResponseDto;
import com.example.models.activity.Activity;
import com.example.models.activity.ActivityStatus;
import com.example.models.activity.ActivityType;
import com.example.models.trivia.Option;
import com.example.models.trivia.TriviaQuestion;
import com.example.repository.activity.ActivityRepository;
import com.example.repository.trivia.TriviaRepository;
import com.example.utils.DtoValidator;

public class TriviaService {

    private final TriviaRepository triviaRepository;
    private final ActivityRepository activityRepository;

    public TriviaService(TriviaRepository triviaRepository, ActivityRepository activityRepository){

        this.triviaRepository = triviaRepository;
        this.activityRepository = activityRepository;
    }

    public void create(String activityId, CreateTriviaDto dto){

        DtoValidator.validate(dto);

        Activity activity =
                activityRepository.findById(activityId)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "La actividad no existe."
                    ));

        if(activity.getType() != ActivityType.TRIVIA){

            throw new IllegalArgumentException("La actividad no es una trivia."
            );
        }

        if(activity.getStatus() != ActivityStatus.DRAFT){

            throw new IllegalArgumentException("No se pueden modificar preguntas publicadas."
            );
        }

        int number = triviaRepository.nextQuestionNumber(activityId);

        TriviaQuestion question =
                new TriviaQuestion(
                    UUID.randomUUID().toString(),
                    activityId,
                    number,
                    dto.getStatement()
                );
        triviaRepository.create(question);

        for(int i = 0; i < dto.getOptions().size(); i++){

            Option option =
                    new Option(
                        UUID.randomUUID().toString(),
                        question.getId(),
                        dto.getOptions().get(i).getText(),
                        i == 0
                    );
            triviaRepository.createOption(option);
        }
    }


    public List<TriviaResponseDto> findAll(String activityId){

        return triviaRepository.findAll(activityId)
                .stream()
                .map(this::map)
                .toList();

    }

    public void delete(String triviaId) {

        TriviaQuestion question = triviaRepository.findById(triviaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pregunta no encontrada."));

        Activity activity = activityRepository.findById(question.getActivityId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Actividad no encontrada."));

        if (activity.getStatus() != ActivityStatus.DRAFT) {
            throw new IllegalArgumentException(
                    "No se pueden eliminar preguntas de una actividad publicada.");
        }

        triviaRepository.delete(triviaId);
        triviaRepository.reorderQuestions(activity.getId());
    }

    private TriviaResponseDto map(
            TriviaQuestion question){

        return new TriviaResponseDto(
                question.getId(),
                question.getQuestionNumber(),
                question.getStatement(),
                question.getOptions()
        );
    }
}