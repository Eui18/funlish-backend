package com.example.repository.scramble;

import java.util.List;
import java.util.Optional;

import com.example.models.scramble.Scramble;

public interface ScrambleRepository {

    void create(Scramble scramble);

    List<Scramble> findAll(String activityId);

    Optional<Scramble> findById(String id);

    boolean delete(String id);

    boolean existsByContent(String activityId, String content);

    int nextNumberChallenge(String activityId);
}