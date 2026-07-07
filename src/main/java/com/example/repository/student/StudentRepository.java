package com.example.repository.student;

import java.util.Optional;
import com.example.models.user.User;

public interface StudentRepository {

    Optional<User> findById(String id);

    boolean joinGroup(String studentId, String groupId);

}