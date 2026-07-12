package com.example.repository.activityStudent;

import java.math.BigDecimal;
import java.util.Optional;

import com.example.dtos.activityStudent.ActivityContentDto;

public interface StudentGameRepository {

    Optional<ActivityContentDto> findTriviaQuestion(String activityId, Integer questionNumber);

    Optional<ActivityContentDto> findScrambleChallenge(String activityId, Integer challengeNumber);

    Optional<String> findTriviaId(String activityId, Integer questionNumber);

    Optional<String> findScrambleId(String activityId, Integer challengeNumber);

    Optional<String> findCorrectTriviaAnswer(String activityId, Integer questionNumber);

    Optional<String> findCorrectScrambleAnswer(String activityId, Integer challengeNumber);

    int countTriviaQuestions(String activityId);

    int countScrambleChallenges(String activityId);

    void saveAnswer(
            String activityStudentId,
            String triviaId,
            String scrambleId,
            Integer number,
            String answer,
            boolean correct,
            BigDecimal points
    );
}