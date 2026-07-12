package com.example.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.example.dtos.activityStudent.ActivityContentDto;
import com.example.dtos.activityStudent.AnswerDto;
import com.example.dtos.activityStudent.ResultDto;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ValidationException;
import com.example.models.activity.Activity;
import com.example.models.activity.ActivityType;
import com.example.models.activityStudent.ActivityStudent;
import com.example.models.activityStudent.ActivityStudentStatus;
import com.example.repository.activity.ActivityRepository;
import com.example.repository.activityStudent.ActivityStudentRepository;
import com.example.repository.activityStudent.StudentGameRepository;

public class StudentGameService {

    private final StudentGameRepository repository;
    private final ActivityStudentRepository activityStudentRepository;
    private final ActivityRepository activityRepository;

    public StudentGameService(
            StudentGameRepository repository,
            ActivityStudentRepository activityStudentRepository,
            ActivityRepository activityRepository) {

        this.repository = repository;
        this.activityStudentRepository = activityStudentRepository;
        this.activityRepository = activityRepository;
    }


    public ActivityContentDto getCurrentContent(String activityStudentId) {

        ActivityStudent attempt = activityStudentRepository.findById(activityStudentId)
                .orElseThrow(() -> 
                        new NotFoundException("Intento no encontrado."));

        Activity activity = activityRepository.findById(attempt.getActivityId())
                .orElseThrow(() ->
                        new NotFoundException("Actividad no encontrada."));

        if(activity.getType() == ActivityType.TRIVIA) {

            return repository.findTriviaQuestion( activity.getId(), attempt.getLastQuestion()
            ).orElseThrow(() ->
                    new NotFoundException("Pregunta no encontrada."));
        }

        return repository.findScrambleChallenge(activity.getId(), attempt.getLastQuestion()
        ).orElseThrow(() ->
                new NotFoundException("Reto no encontrado."));
    }


    public ResultDto answerTrivia(String activityStudentId, AnswerDto dto) {

        ActivityStudent attempt = activityStudentRepository.findById(activityStudentId)
                .orElseThrow(() ->
                        new NotFoundException("Intento no encontrado."));
        
        if(!attempt.getLastQuestion().equals(dto.getNumber())){

                throw new ValidationException(List.of("La pregunta enviada no corresponde al progreso actual."));
        }

        validateCompleted(attempt);

        Activity activity = activityRepository.findById(attempt.getActivityId())
                .orElseThrow(() ->
                        new NotFoundException("Actividad no encontrada."));

        String triviaId = repository.findTriviaId(activity.getId(), dto.getNumber()
                ).orElseThrow(() ->
                        new NotFoundException("Pregunta no encontrada."));

        String correctAnswer = repository.findCorrectTriviaAnswer(activity.getId(), dto.getNumber()
                ).orElseThrow(() ->
                        new NotFoundException(
                                "Respuesta correcta no encontrada."));

        boolean correct = correctAnswer.trim().equalsIgnoreCase(dto.getAnswer().trim());

        BigDecimal points = correct ? BigDecimal.valueOf(activity.getScorePerQuestion()): BigDecimal.ZERO;

        repository.saveAnswer(
                activityStudentId,
                triviaId,
                null,
                dto.getNumber(),
                dto.getAnswer(),
                correct,
                points
        );

        attempt.setScore(attempt.getScore().add(points));

        int totalQuestions = repository.countTriviaQuestions(activity.getId());

        boolean finished = dto.getNumber() >= totalQuestions;

        updateProgress(
                attempt,
                finished,
                dto.getNumber()
        );

        return new ResultDto(
                correct,
                points,
                finished,
                attempt.getLastQuestion()
        );
    }


    public ResultDto answerScramble(String activityStudentId, AnswerDto dto) {

    ActivityStudent attempt = activityStudentRepository.findById(activityStudentId)
            .orElseThrow(() ->
                    new NotFoundException("Intento no encontrado.")
            );

    if(!attempt.getLastQuestion().equals(dto.getNumber())) {

        throw new ValidationException(
                List.of("El reto enviado no corresponde al progreso actual.")
        );
    }

    validateCompleted(attempt);

    Activity activity = activityRepository.findById(attempt.getActivityId())
            .orElseThrow(() ->
                    new NotFoundException("Actividad no encontrada.")
            );

    String scrambleId = repository.findScrambleId( activity.getId(), dto.getNumber()
    ).orElseThrow(() ->
            new NotFoundException("Reto no encontrado.")
    );

    String correctAnswer = repository.findCorrectScrambleAnswer(activity.getId(), dto.getNumber()
            ).orElseThrow(() ->
                    new NotFoundException( "Respuesta correcta no encontrada." )
            );

    boolean correct = normalize(correctAnswer).equals(normalize(dto.getAnswer()));

    BigDecimal points = correct ? BigDecimal.valueOf(activity.getScorePerQuestion()) : BigDecimal.ZERO;

    repository.saveAnswer(
            activityStudentId,
            null,
            scrambleId,
            dto.getNumber(),
            dto.getAnswer(),
            correct,
            points
    );

    attempt.setScore(attempt.getScore().add(points));

    int totalChallenges = repository.countScrambleChallenges(activity.getId());

    boolean finished = dto.getNumber() >= totalChallenges;

    updateProgress( attempt, finished, dto.getNumber() );

    return new ResultDto(
correct,
            points,
            finished,
            attempt.getLastQuestion()
    );
}


    private void updateProgress(ActivityStudent attempt, boolean finished, Integer currentNumber) {

        if(finished) {
            finishActivity(attempt);
        } else {
            attempt.setLastQuestion(currentNumber + 1);
            activityStudentRepository.update(attempt);
        }
    }


    public void finishActivity(ActivityStudent attempt) {
        LocalDateTime now = LocalDateTime.now();

        int minutes = (int) Duration.between(attempt.getStartDate(), now ).toMinutes();

        attempt.setTimeSpent(minutes);
        attempt.setDeliveryDate(now);
        attempt.setStatus(ActivityStudentStatus.COMPLETADA);

        Activity activity = activityRepository.findById(attempt.getActivityId()
                ).orElseThrow(() ->
                        new NotFoundException(
                                "Actividad no encontrada."));

        BigDecimal bonus = calculateBonus(attempt.getScore(), activity.getDurationMinutes(), minutes);

        attempt.setBonusPoints(bonus);
        activityStudentRepository.update(attempt);
    }


    private BigDecimal calculateBonus(BigDecimal score, Integer duration, Integer usedTime) {

        if(score.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        if(usedTime >= duration) {
            return BigDecimal.ZERO;
        }

        double remaining = (double)(duration - usedTime) / duration;

        return score.multiply(BigDecimal.valueOf(remaining));
    }


    private void validateCompleted(ActivityStudent attempt) {

        if(attempt.getStatus() == ActivityStudentStatus.COMPLETADA) {

            throw new ValidationException(List.of("La actividad ya fue completada.")
            );
        }
    }


    private String normalize(String text) {

        if(text == null) {
            return "";
        }

        return text.trim().replaceAll("\\s+", " ").toLowerCase();
    }
}