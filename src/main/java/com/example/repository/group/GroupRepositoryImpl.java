package com.example.repository.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.dtos.users.UserResponseDto;
import com.example.models.group.Group;
import com.example.models.user.Role;

public class GroupRepositoryImpl implements GroupRepository{

    private final Connection connection;

    public GroupRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    //docente
    @Override
    public List<Group> findAll(String teacherId) {
        List<Group> list = new ArrayList<>();
        String sql = """
                    SELECT
                    id, 
                    nombre AS name, 
                    cuatrimestre AS semester, 
                    grupo AS group,
                    codigo_acceso AS accessCode,
                    id_docente AS teacherId
                    FROM GRUPO 
                    WHERE id_docente = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            
            statement.setString(1, teacherId);

            try (ResultSet resultSet = statement.executeQuery();) {
                while (resultSet.next()) {
                    list.add(mapGroup(resultSet));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al listar los grupos" + e.getMessage(), e);
        }
        return list;
    }



    @Override
    public Optional<Group> findById(String id) {
        String sql = """
                    SELECT
                    id, 
                    nombre AS name, 
                    cuatrimestre AS semester, 
                    grupo AS group,
                    codigo_acceso AS accessCode,
                    id_docente AS teacherId
                    FROM GRUPO 
                    WHERE id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery();) {
                
                if (resultSet.next()) {
                    return Optional.of(mapGroup(resultSet));
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al listar los grupos" + e.getMessage(), e);
        }
    }



    @Override
    public Group create(Group group){
        String sql = """
                INSERT INTO grupo(id,id_docente,nombre,cuatrimestre,grupo,codigo_acceso)
                VALUES(?,?,?,?,?,?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, group.getId());
            statement.setString(2, group.getTeacherId());
            statement.setString(3, group.getName());
            statement.setInt(4, group.getSemester());
            statement.setString(5, group.getGroup());
            statement.setString(6, group.getAccessCode());

            int rows = statement.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException ("No se pudo crear el grupo");
            }
            return group;

        } catch (Exception e) {
            throw new RuntimeException("Error al crear un grupo: " + e.getMessage(), e);
        }
    }


    @Override
    public boolean delete(String id) {
       
        String sql = "DELETE FROM GRUPO WHERE id = ? ";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);

           int rows = statement.executeUpdate();

            if (rows > 0) {
                return true;
            }
            return false; 

        } catch (Exception e) {
             throw new RuntimeException("Error al eliminar grupo ", e);
        }  
    }


    @Override
    public boolean existsByAccessCode(String code) {

        String sql = """
                SELECT 1
                FROM GRUPO
                WHERE codigo_acceso = ?
                LIMIT 1
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, code);

            return statement.executeQuery().next();

        } catch (Exception e) {
            throw new RuntimeException("Error al verificar código", e);
        }
    }
 
 

   @Override
    public boolean update(Group group) {

        String sql = """
                UPDATE GRUPO
                SET nombre = ?
                WHERE id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, group.getName());
            statement.setString(2, group.getId());

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el grupo", e);
        }
    }

    

    @Override
    public Optional<Group> findExist( Integer semester, String group, String teacherId) {
        String sql = """
                    SELECT
                    id, 
                    nombre AS name, 
                    cuatrimestre AS semester, 
                    grupo AS group,
                    codigo_acceso AS accessCode,
                    id_docente AS teacherId
                    FROM GRUPO 
                    WHERE cuatrimestre = ? and grupo = ? and id_docente = ? 
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,semester);
            statement.setString(2,group);
            statement.setString(3,teacherId);

            try (ResultSet resultSet = statement.executeQuery();) {
                
                if (resultSet.next()) {
                    return Optional.of(mapGroup(resultSet));
                }

                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al verficicar la existencia del grupo " + e.getMessage(), e);
        }
    }

    //general
    @Override
    public Optional<Group> findByAccessCode(String accessCode) {

        String sql = """
                SELECT
                    id,
                    nombre AS name,
                    cuatrimestre AS semester,
                    grupo AS group,
                    codigo_acceso AS accessCode,
                    id_docente AS teacherId
                FROM GRUPO
                WHERE codigo_acceso = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accessCode);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapGroup(resultSet));
                }

                return Optional.empty();
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar grupo por código.", e);
        }
    }

   @Override
    public List<UserResponseDto> findStudents(String groupId) {

        List<UserResponseDto> students = new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    nombre AS name,
                    matricula AS tuition,
                    correo AS email,
                    rol AS role
                FROM USUARIO
                WHERE id_grupo = ?
                AND rol = 'STUDENT'
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, groupId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    students.add(new UserResponseDto(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            resultSet.getString("tuition"),
                            resultSet.getString("email"),
                            Role.valueOf(resultSet.getString("role"))
                    ));
                }
            }
            return students;

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los alumnos del grupo.", e);
        }
    }


    @Override
    public int countStudents(String groupId) {

        String sql = """
                SELECT COUNT(*)
                FROM USUARIO
                WHERE id_grupo = ?
                AND rol = 'STUDENT'
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, groupId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }

                return 0;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al contar alumnos.", e);
        }
    }



    private Group mapGroup(ResultSet resultSet) throws SQLException {

        return new Group(
                resultSet.getString("id"),
                resultSet.getString("name"),
                resultSet.getInt("semester"),
                resultSet.getString("group"),
                resultSet.getString("accessCode"),
                resultSet.getString("teacherId")
        );
    }

}