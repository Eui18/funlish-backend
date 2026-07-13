package com.example.repository.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import com.example.models.user.Role;
import com.example.models.user.User;

public class AuthRepositoryImpl implements AuthRepository {

    private final Connection connection;

    public AuthRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<User> findUser(String tuition, String password) {

        String sql = """
                    SELECT
                        id,
                        nombre AS name,
                        matricula AS tuition,
                        correo AS email,
                        contrasena AS password,
                        rol AS role,
                        id_grupo AS groupId
                    FROM USUARIO
                    WHERE matricula = ? AND contrasena = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, tuition);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User user = new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("tuition"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role")),
                        resultSet.getString("groupId"));

                return Optional.of(user);
            }

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }

    }

    
    @Override
    public User registerUser(User user) {

        String sql = """
                    INSERT INTO USUARIO (id, nombre, matricula, correo, contrasena, rol)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getTuition());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getRole().name());

            int rows = statement.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("No se pudo registrar el usuario");
            }

            return user;

        } catch (Exception e) {
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage(), e);
        }
    }

    

    @Override
    public boolean existsByTuitionOrEmail(String tuition, String email) {
        String sql = "SELECT 1 FROM USUARIO WHERE matricula = ? OR correo = ? LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tuition);
            statement.setString(2, email);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar duplicados: " + e.getMessage(), e);
        }
    }


    @Override
    public Optional<User> findById(String id) {

        String sql = """
                SELECT
                    id,
                    nombre AS name,
                    matricula AS tuition,
                    correo AS email,
                    contrasena AS password,
                    rol AS role,
                    id_grupo AS groupId
                FROM USUARIO
                WHERE id = ?
                """;


        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {


            statement.setString(1, id);


            ResultSet resultSet =
                    statement.executeQuery();


            if (resultSet.next()) {

                User user = new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("tuition"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role")),
                        resultSet.getString("groupId")
                );


                return Optional.of(user);
            }


            return Optional.empty();


        } catch (Exception e) {

            throw new RuntimeException(
                "Error al buscar usuario por id: "
                + e.getMessage(), e
            );
        }
    }


    @Override
    public Optional<User> findByTuition(String tuition) {

        String sql = """
                    SELECT
                        id,
                        nombre AS name,
                        matricula AS tuition,
                        correo AS email,
                        contrasena AS password,
                        rol AS role,
                        id_grupo AS groupId
                    FROM USUARIO
                    WHERE matricula = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, tuition);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User user = new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("tuition"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role")),
                        resultSet.getString("groupId"));

                return Optional.of(user);
            }

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException(
                "Error al buscar usuario por matrícula: "
                + e.getMessage(), e);
        }
    }
}
