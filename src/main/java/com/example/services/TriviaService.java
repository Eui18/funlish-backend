package com.example.services;

import java.util.List;
import java.util.UUID;

import com.example.dtos.trivia.CreateTriviaDto;
import com.example.dtos.trivia.OptionDto;
import com.example.dtos.trivia.TriviaResponseDto;
import com.example.dtos.trivia.UpdateTriviaDto;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ValidationException;
import com.example.models.activity.Activity;
import com.example.models.activity.ActivityStatus;
import com.example.models.activity.ActivityType;
import com.example.models.trivia.Option;
import com.example.models.trivia.TriviaQuestion;
import com.example.models.user.Role;
import com.example.models.user.User;
import com.example.repository.activity.ActivityRepository;
import com.example.repository.auth.AuthRepository;
import com.example.repository.trivia.TriviaRepository;
import com.example.utils.DtoValidator;

public class TriviaService {

    private final TriviaRepository triviaRepository;
    private final ActivityRepository activityRepository;
    private final AuthRepository authRepository;

    public TriviaService(
            TriviaRepository triviaRepository,
            ActivityRepository activityRepository,
            AuthRepository authRepository
    ) {
        this.triviaRepository = triviaRepository;
        this.activityRepository = activityRepository;
        this.authRepository = authRepository;
    }


    public void create(String activityId, CreateTriviaDto dto, String teacherId) {

        User teacher = authRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("El docente no existe."));

        if (teacher.getRole() != Role.TEACHER) {
            throw new ValidationException(List.of("Solo un docente puede crear preguntas."));
        }

        DtoValidator.validate(dto);

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new NotFoundException("La actividad no existe.")
                );

        if(activity.getType() != ActivityType.TRIVIA) {
            throw new ValidationException(
                    List.of("La actividad no pertenece al tipo TRIVIA.")
            );
        }

        if(activity.getStatus() != ActivityStatus.DRAFT) {
            throw new ValidationException(
                    List.of("No se pueden modificar preguntas publicadas.")
            );
        }

        validateOptions(dto.getOptions());

        int number = triviaRepository.nextQuestionNumber(activityId);

        TriviaQuestion question = new TriviaQuestion(
                UUID.randomUUID().toString(),
                activityId,
                number,
                dto.getStatement()
        );

        triviaRepository.create(question);

        for(OptionDto optionDto : dto.getOptions()) {

            Option option = new Option(
                    UUID.randomUUID().toString(),
                    question.getId(),
                    optionDto.getText(),
                    optionDto.getCorrect()
            );

            triviaRepository.createOption(option);
        }
    }


    private void validateOptions(List<OptionDto> options) {

        if(options == null || options.size() < 2) {
            throw new ValidationException(
                    List.of("La pregunta debe tener mínimo 2 opciones.")
            );
        }

        if(options.size() > 4) {
            throw new ValidationException(
                    List.of("La pregunta no puede tener más de 4 opciones.")
            );
        }

        long correctAnswers = options.stream().filter(option -> option.getCorrect()).count();

        if(correctAnswers != 1) {
            throw new ValidationException(
                    List.of("Debe existir exactamente una respuesta correcta.")
            );
        }
    }


    public List<TriviaResponseDto> findAll(String activityId) {

        return triviaRepository.findAll(activityId)
                .stream()
                .map(this::map)
                .toList();
    }


    public void update(String triviaId, UpdateTriviaDto dto) {

        DtoValidator.validate(dto);

        TriviaQuestion question = triviaRepository.findById(triviaId)
                .orElseThrow(() ->
                        new NotFoundException("Pregunta no encontrada.")
                );

        Activity activity = activityRepository.findById(
                question.getActivityId()
        ).orElseThrow(() ->
                new NotFoundException("Actividad no encontrada.")
        );


        if(activity.getStatus() != ActivityStatus.DRAFT) {
                throw new ValidationException(
                        List.of("No se pueden modificar preguntas publicadas.")
                );
        }


        validateOptions(dto.getOptions());


        TriviaQuestion updatedQuestion = new TriviaQuestion(
                question.getId(),
                question.getActivityId(),
                question.getQuestionNumber(),
                dto.getStatement()
        );

        triviaRepository.update(updatedQuestion);


        List<Option> options = dto.getOptions()
                .stream()
                .map(option ->
                        new Option(
                                UUID.randomUUID().toString(),
                                triviaId,
                                option.getText(),
                                option.getCorrect()
                        )
                )
                .toList();


        triviaRepository.updateOptions(triviaId, options);
        }


    public void delete(String triviaId) {

        TriviaQuestion question = triviaRepository.findById(triviaId)
                .orElseThrow(() ->
                        new NotFoundException("Pregunta no encontrada.")
                );

        Activity activity = activityRepository.findById(
                question.getActivityId()
        ).orElseThrow(() ->
                new NotFoundException("Actividad no encontrada.")
        );

        if(activity.getStatus() != ActivityStatus.DRAFT) {
            throw new ValidationException(
                    List.of(
                    "No se pueden eliminar preguntas de una actividad publicada."
                    )
            );
        }

        triviaRepository.delete(triviaId);
        triviaRepository.reorderQuestions(activity.getId());
    }


    private TriviaResponseDto map(TriviaQuestion question) {

        return new TriviaResponseDto(
                question.getId(),
                question.getQuestionNumber(),
                question.getStatement(),
                question.getOptions()
        );
    }
}