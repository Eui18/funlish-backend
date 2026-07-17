package com.example.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.dtos.activity.ActivityResponseDto;
import com.example.dtos.activity.CreateActivityDto;
import com.example.dtos.activity.DuplicateActivityDto;
import com.example.dtos.activity.UpdateActivityDto;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ValidationException;
import com.example.models.activity.Activity;
import com.example.models.activity.ActivityStatus;
import com.example.models.activity.ActivityType;
import com.example.models.topic.Topic;
import com.example.models.trivia.Option;
import com.example.models.trivia.TriviaQuestion;
import com.example.models.scramble.Scramble;
import com.example.models.unit.Unit;
import com.example.models.user.Role;
import com.example.models.user.User;
import com.example.repository.activity.ActivityRepository;
import com.example.repository.auth.AuthRepository;
import com.example.repository.scramble.ScrambleRepository;
import com.example.repository.topic.TopicRepository;
import com.example.repository.trivia.TriviaRepository;
import com.example.repository.unit.UnitRepository;
import com.example.utils.DtoValidator;
import com.example.utils.TextNormalizer;

public class ActivityService {

    private final ActivityRepository activityRepository;
    private final TopicRepository topicRepository;
    private final UnitRepository unitRepository;
    private final AuthRepository authRepository;
    private final TriviaRepository triviaRepository;
    private final ScrambleRepository scrambleRepository;

    public ActivityService(
            ActivityRepository activityRepository,
            TopicRepository topicRepository,
            UnitRepository unitRepository,
            AuthRepository authRepository,
            TriviaRepository triviaRepository,
            ScrambleRepository scrambleRepository) {

        this.activityRepository = activityRepository;
        this.topicRepository = topicRepository;
        this.unitRepository = unitRepository;
        this.authRepository = authRepository;
        this.triviaRepository = triviaRepository;
        this.scrambleRepository = scrambleRepository;
    }


    // Crear actividad
    public void create(CreateActivityDto dto, String teacherId, String topicId) {

        DtoValidator.validate(dto);

        User teacher = authRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("El docente no existe."));

        if (teacher.getRole() != Role.TEACHER) {
            throw new ValidationException(List.of("Solo un docente puede crear actividades."));
        }

        if (topicRepository.findById(topicId).isEmpty()) {
            throw new IllegalArgumentException("El tema no existe.");
        }

        if (activityRepository.existsByTitle(
                topicId,
                dto.getTitle())) {

            throw new IllegalArgumentException(
                    "Ya existe una actividad con ese título.");
        }

        Activity activity = new Activity(
                UUID.randomUUID().toString(),
                topicId,
                dto.getTitle(),
                dto.getDescription(),
                parseType(dto.getType()),
                dto.getDurationMinutes(),
                dto.getScorePerQuestion(),
                ActivityStatus.DRAFT,
                LocalDateTime.now(),
                parseDate(dto.getStartDate(), "La fecha de inicio no es válida."),
                parseDate(dto.getEndDate(), "La fecha final no es válida."),
                parseTime(dto.getStartTime(), "La hora de inicio no es válida."),
                parseTime(dto.getEndTime(), "La hora final no es válida.")
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

        boolean isDraft = activity.getStatus() == ActivityStatus.DRAFT;

        if (dto.getTitle() != null && !dto.getTitle().equalsIgnoreCase(activity.getTitle()) && activityRepository.existsByTitle(
                activity.getTopicId(),
                dto.getTitle())) {

            throw new IllegalArgumentException(
                    "Ya existe una actividad con ese título.");
        }

        boolean touchesCoreFields = dto.getType() != null
                || dto.getDurationMinutes() != null
                || dto.getScorePerQuestion() != null;

        if (touchesCoreFields && !isDraft) {

            throw new IllegalArgumentException(
                    "El tipo, el puntaje y la duración solo se pueden modificar mientras la actividad esté en borrador."
            );
        }

        if (dto.getTitle() != null) {
            activity.setTitle(dto.getTitle());
        }

        if (dto.getDescription() != null) {
            activity.setDescription(dto.getDescription());
        }

        if (dto.getType() != null) {
            activity.setType(parseType(dto.getType()));
        }

        if (dto.getDurationMinutes() != null) {
            activity.setDurationMinutes(dto.getDurationMinutes());
        }

        if (dto.getScorePerQuestion() != null) {
            activity.setScorePerQuestion(dto.getScorePerQuestion());
        }

        if (dto.getStartDate() != null) {
            activity.setStartDate(parseDate(dto.getStartDate(), "La fecha de inicio no es válida."));
        }

        if (dto.getEndDate() != null) {
            activity.setEndDate(parseDate(dto.getEndDate(), "La fecha final no es válida."));
        }

        if (dto.getStartTime() != null) {
            activity.setStartTime(parseTime(dto.getStartTime(), "La hora de inicio no es válida."));
        }

        if (dto.getEndTime() != null) {
            activity.setEndTime(parseTime(dto.getEndTime(), "La hora final no es válida."));
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
            throw new IllegalArgumentException("La actividad ya fue publicada.");
        }

        if (activity.getStatus() == ActivityStatus.FINISHED) {
            throw new IllegalArgumentException("La actividad ya finalizó y no puede publicarse de nuevo.");
        }

        boolean hasQuestions = activity.getType() == ActivityType.TRIVIA
                ? !triviaRepository.findAll(id).isEmpty()
                : !scrambleRepository.findAll(id).isEmpty();

        if (!hasQuestions) {
            throw new IllegalArgumentException("No se puede publicar una actividad sin preguntas.");
        }

        if (activity.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("La duración de la actividad no es válida.");
        }

        if (activity.getStartDate() == null
                || activity.getEndDate() == null
                || activity.getStartTime() == null
                || activity.getEndTime() == null) {

            throw new IllegalArgumentException("Debes definir fecha y hora de inicio y de fin antes de publicar.");
        }

        LocalDateTime start = LocalDateTime.of(activity.getStartDate(), activity.getStartTime());
        LocalDateTime end = LocalDateTime.of(activity.getEndDate(), activity.getEndTime());

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("La fecha y hora final debe ser posterior a la de inicio.");
        }

        if (start.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La hora de inicio ya quedó en el pasado. Actualiza el horario antes de publicar.");
        }

        activityRepository.publish(id);
    }



    // Eliminar
    public void delete(String id) {

        activityRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Actividad no encontrada."));

        if (activityRepository.hasStudentAttempts(id)) {
            throw new IllegalArgumentException("No es posible eliminar una actividad con intentos registrados.");
        }

        activityRepository.delete(id);
    }



    // Buscar por id
    public ActivityResponseDto findById(String id) {

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Actividad no encontrada."));

        return map(activity);
    }



    // Listar actividades por tema
    public List<ActivityResponseDto> findAll(String topicId) {

        return activityRepository.findAll(topicId)
                .stream()
                .map(this::map)
                .toList();
    }



    // Finaliza automáticamente las actividades PUBLISHED cuya disponibilidad ya venció.
    // Llamado únicamente por ActivityStatusScheduler.
    public void finishExpiredActivities() {

        for (Activity activity : activityRepository.findExpiredPublished()) {
            activityRepository.finish(activity.getId());
        }
    }



    // Duplicar actividad hacia otro grupo
    public ActivityResponseDto duplicate(String activityId, DuplicateActivityDto dto, String teacherId) {

        DtoValidator.validate(dto);

        User teacher = authRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("El docente no existe."));

        if (teacher.getRole() != Role.TEACHER) {
            throw new ValidationException(List.of("Solo un docente puede duplicar actividades."));
        }

        Activity original = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Actividad no encontrada."));

        Topic originalTopic = topicRepository.findById(original.getTopicId())
                .orElseThrow(() -> new NotFoundException("El tema original no existe."));

        Topic targetTopic = findEquivalentTopic(originalTopic.getTitle(), dto.getTargetGroupId())
                .orElseThrow(() -> new ValidationException(
                        List.of("No existe un tema compatible en el grupo destino. Cree primero el tema y vuelva a intentar.")
                ));

        if (activityRepository.existsByTitle(targetTopic.getId(), original.getTitle())) {
            throw new IllegalArgumentException("Ya existe una actividad con ese título en el tema destino.");
        }

        Activity copy = new Activity(
                UUID.randomUUID().toString(),
                targetTopic.getId(),
                original.getTitle(),
                original.getDescription(),
                original.getType(),
                original.getDurationMinutes(),
                original.getScorePerQuestion(),
                ActivityStatus.DRAFT,
                LocalDateTime.now(),
                original.getStartDate(),
                original.getEndDate(),
                original.getStartTime(),
                original.getEndTime()
        );

        activityRepository.create(copy);

        if (original.getType() == ActivityType.TRIVIA) {
            duplicateTrivia(original.getId(), copy.getId());
        } else {
            duplicateScramble(original.getId(), copy.getId());
        }

        return map(copy);
    }


    private void duplicateTrivia(String originalActivityId, String newActivityId) {

        for (TriviaQuestion question : triviaRepository.findAll(originalActivityId)) {

            TriviaQuestion newQuestion = new TriviaQuestion(
                    UUID.randomUUID().toString(),
                    newActivityId,
                    question.getQuestionNumber(),
                    question.getStatement()
            );

            triviaRepository.create(newQuestion);

            for (Option option : question.getOptions()) {

                triviaRepository.createOption(new Option(
                        UUID.randomUUID().toString(),
                        newQuestion.getId(),
                        option.getText(),
                        option.isCorrect()
                ));
            }
        }
    }


    private void duplicateScramble(String originalActivityId, String newActivityId) {

        for (Scramble scramble : scrambleRepository.findAll(originalActivityId)) {

            scrambleRepository.create(new Scramble(
                    UUID.randomUUID().toString(),
                    newActivityId,
                    scramble.getNumberChallenge(),
                    scramble.getCorrectContent(),
                    scramble.getType()
            ));
        }
    }


    private Optional<Topic> findEquivalentTopic(String originalTitle, String targetGroupId) {

        String target = TextNormalizer.normalize(originalTitle);

        for (Unit unit : unitRepository.findAll(targetGroupId)) {
            for (Topic topic : topicRepository.findAll(unit.getId())) {

                if (TextNormalizer.normalize(topic.getTitle()).equals(target)) {
                    return Optional.of(topic);
                }
            }
        }

        return Optional.empty();
    }


    private ActivityType parseType(String type) {

        try {
            return ActivityType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Tipo de actividad no válido.");
        }
    }


    private LocalDate parseDate(String value, String errorMessage) {

        if (value == null) {
            return null;
        }

        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }


    private LocalTime parseTime(String value, String errorMessage) {

        if (value == null) {
            return null;
        }

        try {
            return LocalTime.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }


    private ActivityResponseDto map(Activity activity) {

        return new ActivityResponseDto(
                activity.getId(),
                activity.getTopicId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getType().name(),
                activity.getDurationMinutes(),
                activity.getScorePerQuestion(),
                activity.getStatus().name(),
                activity.getStartDate() != null ? activity.getStartDate().toString() : null,
                activity.getEndDate() != null ? activity.getEndDate().toString() : null,
                activity.getStartTime() != null ? activity.getStartTime().toString() : null,
                activity.getEndTime() != null ? activity.getEndTime().toString() : null
        );
    }
}