package com.example.repository.activity;

import java.util.List;
import java.util.Optional;

import com.example.models.activity.Activity;

public interface ActivityRepository {

    void create(Activity activity);

    void update(Activity activity);

    void publish(String activityId);

    void finish(String activityId);

    List<Activity> findExpiredPublished();

    void delete(String id);

    Optional<Activity> findById(String id);

    List<Activity> findAll(String topicId);

    boolean existsByTitle(String topicId, String title);

    boolean hasStudentAttempts(String activityId);
}