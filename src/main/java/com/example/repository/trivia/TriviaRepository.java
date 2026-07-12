package com.example.repository.trivia;

import java.util.List;
import java.util.Optional;

import com.example.models.trivia.Option;
import com.example.models.trivia.TriviaQuestion;

public interface TriviaRepository {
    
    int nextQuestionNumber(String activityId);

    void create(TriviaQuestion question);

    void createOption(Option option);

    Optional<TriviaQuestion> findById(String id);

    void delete(String id);
    
    void reorderQuestions(String activityId);

    List<TriviaQuestion> findAll(String activityId);

    void update(TriviaQuestion question);

    void updateOptions(String triviaId, List<Option> options);

}