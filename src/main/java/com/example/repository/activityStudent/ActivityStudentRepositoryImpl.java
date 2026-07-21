package com.example.repository.activityStudent;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

import com.example.exceptions.ResourceAlreadyExistsException;
import com.example.models.activityStudent.ActivityStudent;
import com.example.models.activityStudent.ActivityStudentStatus;

public class ActivityStudentRepositoryImpl implements ActivityStudentRepository {

    private final DataSource dataSource;

    public ActivityStudentRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(ActivityStudent activityStudent) {

        String sql = """
                INSERT INTO actividad_alumno
                (
                    id,
                    id_actividad,
                    id_alumno,
                    estado,
                    puntaje,
                    bonus_puntos,
                    fecha_inicio,
                    ultima_pregunta
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)
        ){

            ps.setString(1, activityStudent.getId());
            ps.setString(2, activityStudent.getActivityId());
            ps.setString(3, activityStudent.getStudentId());

            ps.setString(
                4,
                activityStudent.getStatus().name()
            );

            ps.setBigDecimal(
                5,
                activityStudent.getScore()
            );

            ps.setBigDecimal(
                6,
                activityStudent.getBonusPoints()
            );

            ps.setTimestamp(
                7,
                Timestamp.valueOf(
                    activityStudent.getStartDate()
                )
            );

            ps.setInt(
                8,
                activityStudent.getLastQuestion()
            );

            ps.executeUpdate();

        }catch(SQLException e){

            // La restricción UNIQUE(id_actividad, id_alumno) es la fuente de
            // verdad ante carreras: si otra petición concurrente ya creó el
            // intento, el INSERT viola la restricción. Se señala como "ya
            // existe" para que el servicio lo recupere de forma idempotente,
            // en lugar de devolver un 500.
            if (e instanceof java.sql.SQLIntegrityConstraintViolationException
                    || (e.getSQLState() != null && e.getSQLState().startsWith("23"))) {

                throw new ResourceAlreadyExistsException(
                    "El alumno ya tiene un intento para esta actividad."
                );
            }

            throw new RuntimeException(
                "Error al crear intento: "
                + e.getMessage()
            );
        }
    }



    @Override
    public Optional<ActivityStudent> findById(String id){

        String sql = """
            SELECT
                id,
                id_actividad,
                id_alumno,
                estado,
                puntaje,
                bonus_puntos,
                tiempo_empleado,
                fecha_inicio,
                fecha_entrega,
                ultima_pregunta
            FROM actividad_alumno
            WHERE id = ?
                """;
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps =
                connection.prepareStatement(sql)
        ){
            ps.setString(1,id);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()){
                return Optional.of(map(rs));
            }

        }catch(SQLException e){

            throw new RuntimeException(
                "Error buscando intento: "
                + e.getMessage()
            );
        }
        return Optional.empty();
    }



    @Override
    public Optional<ActivityStudent> findByStudentAndActivity(String studentId, String activityId){

        String sql = """
            SELECT
                id,
                id_actividad,
                id_alumno,
                estado,
                puntaje,
                bonus_puntos,
                tiempo_empleado,
                fecha_inicio,
                fecha_entrega,
                ultima_pregunta
            FROM actividad_alumno
            WHERE id_alumno = ?
            AND id_actividad = ?
                """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement ps =
                connection.prepareStatement(sql)
        ){

            ps.setString(1,studentId);
            ps.setString(2,activityId);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()){
                return Optional.of(map(rs));
            }

        }catch(SQLException e){

            throw new RuntimeException(
                "Error verificando actividad: "
                + e.getMessage()
            );
        }

        return Optional.empty();
    }



    @Override
    public boolean update(ActivityStudent activityStudent){

        String sql = """
                UPDATE actividad_alumno
                SET
                    estado = ?,
                    puntaje = ?,
                    bonus_puntos = ?,
                    tiempo_empleado = ?,
                    fecha_entrega = ?,
                    ultima_pregunta = ?
                WHERE id = ?
                """;
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps =
                connection.prepareStatement(sql)
        ){

            ps.setString(
                1,
                activityStudent.getStatus().name()
            );

            ps.setBigDecimal(
                2,
                activityStudent.getScore()
            );

            ps.setBigDecimal(
                3,
                activityStudent.getBonusPoints()
            );

            ps.setObject(
                4,
                activityStudent.getTimeSpent()
            );

            ps.setTimestamp(
                5,
                activityStudent.getDeliveryDate() == null
                ? null
                : Timestamp.valueOf(
                    activityStudent.getDeliveryDate()
                )
            );

            ps.setInt(
                6,
                activityStudent.getLastQuestion()
            );

            ps.setString(
                7,
                activityStudent.getId()
            );


            return ps.executeUpdate() > 0;

        }catch(SQLException e){

            throw new RuntimeException(
                "Error actualizando intento: "
                + e.getMessage()
            );
        }
    }



    private ActivityStudent map(ResultSet rs)
            throws SQLException {


        return new ActivityStudent(

            rs.getString("id"),
            rs.getString("id_actividad"),
            rs.getString("id_alumno"),

            ActivityStudentStatus.valueOf(
                rs.getString("estado")
            ),

            rs.getBigDecimal("puntaje"),
            rs.getBigDecimal("bonus_puntos"),
            rs.getObject(
                "tiempo_empleado",
                Integer.class
            ),

            rs.getTimestamp(
                "fecha_inicio"
            ) == null
            ? null
            :
            rs.getTimestamp(
                "fecha_inicio"
            ).toLocalDateTime(),


            rs.getTimestamp(
                "fecha_entrega"
            ) == null
            ? null
            :
            rs.getTimestamp(
                "fecha_entrega"
            ).toLocalDateTime(),


            rs.getInt(
                "ultima_pregunta"
            )
        );
    }
}