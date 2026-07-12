package com.example.repository.activityStudent;

import java.util.Optional;

import com.example.models.activityStudent.ActivityStudent;

public interface ActivityStudentRepository {

    void create(ActivityStudent activityStudent);

    Optional<ActivityStudent> findById(String id);

    Optional<ActivityStudent> findByStudentAndActivity(String studentId, String activityId);

    boolean update(ActivityStudent activityStudent);

}