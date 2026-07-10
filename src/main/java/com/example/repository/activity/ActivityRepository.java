package com.example.repository.activity;

import java.util.List;
import java.util.Optional;

import com.example.models.activity.Activity;

public interface ActivityRepository {

    Activity create(Activity activity);

    List<Activity> findAll(String topicId);

    Optional<Activity> findById(String id);

    boolean update(Activity activity);

    boolean delete(String id);

    boolean existsByTitle(String topicId, String title);

    boolean existsByTitle(String topicId, String title, String activityId);

    boolean hasStudentAttempts(String activityId);
}