package com.example.repository.auth;

import java.util.Optional;

import com.example.models.user.User;

public interface AuthRepository {

    User registerUser(User user);
    Optional<User> findUser(String tuition, String password);
    Optional<User> findByTuition(String tuition);
    Optional<User> findById(String id);
    boolean existsByTuitionOrEmail(String tuition, String email);

}
