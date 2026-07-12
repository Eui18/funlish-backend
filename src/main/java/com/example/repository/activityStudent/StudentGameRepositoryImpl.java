package com.example.repository.activityStudent;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.dtos.activityStudent.ActivityContentDto;

public class StudentGameRepositoryImpl implements StudentGameRepository {

    private final Connection connection;

    public StudentGameRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<ActivityContentDto> findTriviaQuestion(
            String activityId,
            Integer questionNumber) {

        String sql = """
                SELECT id, enunciado
                FROM actividad_trivia
                WHERE id_actividad = ?
                AND numero_pregunta = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, activityId);
            ps.setInt(2, questionNumber);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            String triviaId = rs.getString("id");
            String statement = rs.getString("enunciado");

            List<String> options = findOptions(triviaId);

            return Optional.of(
                    new ActivityContentDto(
                            activityId,
                            "TRIVIA",
                            questionNumber,
                            statement,
                            options
                    )
            );

        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo pregunta: " + e.getMessage());
        }
    }

    private List<String> findOptions(String triviaId) {

        String sql = """
                SELECT opcion
                FROM opcion
                WHERE id_trivia = ?
                """;

        List<String> options = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, triviaId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                options.add(rs.getString("opcion"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo opciones: " + e.getMessage());
        }

        Collections.shuffle(options);

        return options;
    }



    @Override
    public Optional<String> findTriviaId(
            String activityId,
            Integer questionNumber) {

        String sql = """
                SELECT id
                FROM actividad_trivia
                WHERE id_actividad = ?
                AND numero_pregunta = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, activityId);
            ps.setInt(2, questionNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(rs.getString("id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error obteniendo id de la pregunta: " + e.getMessage());
        }

        return Optional.empty();
    }



    @Override
    public Optional<String> findScrambleId(
            String activityId,
            Integer challengeNumber) {

        String sql = """
                SELECT id
                FROM actividad_scramble
                WHERE id_actividad = ?
                AND numero_reto = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, activityId);
            ps.setInt(2, challengeNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(rs.getString("id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error obteniendo id del reto: " + e.getMessage());
        }

        return Optional.empty();
    }


    @Override
    public Optional<ActivityContentDto> findScrambleChallenge(
            String activityId,
            Integer challengeNumber) {

        String sql = """
                SELECT contenido_correcto
                FROM actividad_scramble
                WHERE id_actividad = ?
                AND numero_reto = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, activityId);
            ps.setInt(2, challengeNumber);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            String content = rs.getString("contenido_correcto");

            List<String> options = scramble(content);

            return Optional.of(
                    new ActivityContentDto(
                            activityId,
                            "SCRAMBLE",
                            challengeNumber,
                            "Ordena correctamente",
                            options
                    )
            );

        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo reto scramble: " + e.getMessage());
        }
    }

    private List<String> scramble(String content) {

        List<String> parts;

        if (content.trim().contains(" ")) {
            parts = new ArrayList<>(List.of(content.trim().split("\\s+")));
        } else {
            parts = new ArrayList<>();

            for (char c : content.toCharArray()) {
                parts.add(String.valueOf(c));
            }
        }

        Collections.shuffle(parts);

        return parts;
    }



    @Override
    public Optional<String> findCorrectTriviaAnswer(
            String activityId,
            Integer questionNumber) {

        String sql = """
                SELECT o.opcion
                FROM actividad_trivia t
                INNER JOIN opcion o
                    ON o.id_trivia = t.id
                WHERE t.id_actividad = ?
                AND t.numero_pregunta = ?
                AND o.correcta = true
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, activityId);
            ps.setInt(2, questionNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(rs.getString("opcion"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error obteniendo respuesta correcta: " + e.getMessage());
        }

        return Optional.empty();
    }


    @Override
    public Optional<String> findCorrectScrambleAnswer(
            String activityId,
            Integer challengeNumber) {

        String sql = """
                SELECT contenido_correcto
                FROM actividad_scramble
                WHERE id_actividad = ?
                AND numero_reto = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, activityId);
            ps.setInt(2, challengeNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(rs.getString("contenido_correcto"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error obteniendo respuesta correcta: " + e.getMessage());
        }

        return Optional.empty();
    }



    @Override
    public int countTriviaQuestions(String activityId) {

        String sql = """
                SELECT COUNT(*)
                FROM actividad_trivia
                WHERE id_actividad = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, activityId);

            ResultSet rs = ps.executeQuery();

            rs.next();

            return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error contando preguntas: " + e.getMessage());
        }
    }



    @Override
    public int countScrambleChallenges(String activityId) {

        String sql = """
                SELECT COUNT(*)
                FROM actividad_scramble
                WHERE id_actividad = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, activityId);

            ResultSet rs = ps.executeQuery();

            rs.next();

            return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error contando retos: " + e.getMessage());
        }
    }



    @Override
    public void saveAnswer(
            String activityStudentId,
            String triviaId,
            String scrambleId,
            Integer number,
            String answer,
            boolean correct,
            BigDecimal points) {

        String sql = """
                INSERT INTO respuesta_alumno(
                    id,
                    id_actividad_alumno,
                    id_trivia,
                    id_scramble,
                    respuesta,
                    numero,
                    puntos,
                    correcta
                )
                VALUES(?,?,?,?,?,?,?,?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, java.util.UUID.randomUUID().toString());
            ps.setString(2, activityStudentId);
            ps.setString(3, triviaId);
            ps.setString(4, scrambleId);
            ps.setString(5, answer);
            ps.setInt(6, number);
            ps.setBigDecimal(7, points);
            ps.setBoolean(8, correct);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error guardando respuesta: " + e.getMessage());
        }
    }
}