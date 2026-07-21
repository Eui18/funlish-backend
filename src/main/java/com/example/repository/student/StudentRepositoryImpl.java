package com.example.repository.student;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import com.example.models.user.Role;
import com.example.models.user.User;

public class StudentRepositoryImpl implements StudentRepository  {
    
    private final DataSource dataSource;

    public StudentRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
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
                FROM usuario
                WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

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
                UPDATE usuario
                SET id_grupo = ?
                WHERE id = ? and rol = 'STUDENT'
                """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, groupId);
            statement.setString(2, studentId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Error al unir al estudiante al grupo.", e);
        }
    }


    @Override
    public boolean leaveGroup(String studentId) {

        String sql = """
                UPDATE usuario
                SET id_grupo = NULL
                WHERE id = ? and rol = 'STUDENT'
                """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, studentId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Error al retirar al estudiante del grupo.", e);
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