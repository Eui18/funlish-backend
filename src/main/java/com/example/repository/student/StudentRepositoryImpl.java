package com.example.repository.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import com.example.models.user.Role;
import com.example.models.user.User;

public class StudentRepositoryImpl implements StudentRepository  {
    
    private final Connection connection;

    public StudentRepositoryImpl(Connection connection) {
        this.connection = connection;
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

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapUser(resultSet));
                }

                return Optional.empty();
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar estudiante.", e);
        }
    }


    @Override
    public boolean joinGroup(String studentId, String groupId) {

        String sql = """
                UPDATE USUARIO
                SET id_grupo = ?
                WHERE id = ? and rol = 'STUDENT'
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, groupId);
            statement.setString(2, studentId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Error al unir al estudiante al grupo.", e);
        }
    }

    private User mapUser(ResultSet resultSet) throws SQLException {

        return new User(
                resultSet.getString("id"),
                resultSet.getString("name"),
                resultSet.getString("tuition"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role")),
                resultSet.getString("groupId")
        );
    }
}
