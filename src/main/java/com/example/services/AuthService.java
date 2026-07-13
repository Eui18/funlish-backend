package com.example.services;

import java.util.List;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import com.example.dtos.users.CreateUserDto;
import com.example.dtos.users.LoginUserDto;
import com.example.dtos.users.UserResponseDto;
import com.example.dtos.users.LoginResponseDto;
import com.example.exceptions.ResourceAlreadyExistsException;
import com.example.exceptions.UnauthorizedException;
import com.example.models.user.Role;
import com.example.models.user.User;
import com.example.repository.auth.AuthRepository;
import com.example.exceptions.ValidationException;

public class AuthService {

    private final AuthRepository repository;
    private final JwtService jwtService;


    public AuthService(AuthRepository repository, JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    public UserResponseDto registerUser(CreateUserDto dto) {

        Role role;
        try {
            role = Role.valueOf(dto.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException(List.of("Rol inválido: " + dto.getRole()));
        }

        switch (role) {
            case STUDENT:
                validateStudent(dto);
                break;

            case TEACHER:
                validateTeacher(dto);
                break;
        }

        if (repository.existsByTuitionOrEmail(dto.getTuition(), dto.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "Ya existe un usuario registrado con esta matrícula o correo electrónico.");
        }

        //Encriptar contraseña antes de guardarla
        String encryptedPassword =
                BCrypt.hashpw(
                        dto.getPassword(),
                        BCrypt.gensalt()
                );

        User newUser = new User(
                UUID.randomUUID().toString(),
                dto.getName(),
                dto.getTuition(),
                dto.getEmail(),
                encryptedPassword, 
                role);

        User savedUser = repository.registerUser(newUser);

        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getTuition(),
                savedUser.getEmail(),
                savedUser.getRole());
    }


    public LoginResponseDto findUser(LoginUserDto dto) {

        User user = repository.findByTuition(dto.getTuition())
                .orElseThrow(() ->
                        new UnauthorizedException("Usuario no encontrado.")
                );

        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Credenciales inválidas.");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getRole().name()
        );

        return new LoginResponseDto(
                user.getId(),
                user.getName(),
                user.getTuition(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }


    private void validateStudent(CreateUserDto dto) {

        if (!dto.getTuition().matches("\\d{6}")) {
            throw new ValidationException(
                List.of("La matrícula del alumno debe tener exactamente 6 dígitos.")
            );
        }

        if (!dto.getEmail().matches("^\\d{6}@.+$")) {
            throw new ValidationException(
                List.of("El correo del alumno debe iniciar con la matrícula.")
            );
        }

        String emailPrefix = dto.getEmail().split("@")[0];

        if (!emailPrefix.equals(dto.getTuition())) {
            throw new ValidationException(
                List.of("La matrícula no coincide con el correo institucional.")
            );
        }
    }


    private void validateTeacher(CreateUserDto dto) {

        if (!dto.getTuition().matches("\\d{4}")) {
            throw new ValidationException(
                List.of("La matrícula del docente debe tener exactamente 4 dígitos.")
            );
        }

        if (!dto.getEmail().matches("^[A-Za-z]+@.+$")) {
            throw new ValidationException(
                List.of("El correo del docente debe iniciar con letras.")
            );
        }
    }
}