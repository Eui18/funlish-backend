package com.example.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.dtos.topic.CreateTopicDto;
import com.example.dtos.topic.TopicResponseDto;
import com.example.dtos.topic.UpdateTopicDto;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ValidationException;
import com.example.models.topic.Topic;
import com.example.repository.topic.TopicRepository;
import com.example.repository.unit.UnitRepository;

public class TopicService {

    private final TopicRepository repository;
    private final UnitRepository unitRepository;

    public TopicService(TopicRepository repository, UnitRepository unitRepository) {
        this.repository = repository;
        this.unitRepository = unitRepository;
    }

    public TopicResponseDto create(String unitId, CreateTopicDto dto) {

        String title = dto.getTitle().trim();

        if (repository.existsTitle(unitId, title)) {
            throw new ValidationException(
                    List.of("Ya existe un tema con ese título dentro de la unidad.")
            );
        }

        Topic topic = new Topic(
                UUID.randomUUID().toString(),
                unitId,
                repository.nextNumber(unitId),
                title,
                dto.getDescription()
        );

        repository.create(topic);

        return toResponseDto(topic);
    }

    public List<TopicResponseDto> findAll(String unitId) {

        return repository.findAll(unitId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public TopicResponseDto findById(String id) {

        Topic topic = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Tema no encontrado.")
                );

        return toResponseDto(topic);
    }

    public TopicResponseDto update(String id, UpdateTopicDto dto) {

        Topic topic = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Tema no encontrado.")
                );

        if (dto.getTitle() != null) {
            String title = dto.getTitle().trim();
            
            if (title.isBlank()) {
                throw new ValidationException(List.of("El título del tema es obligatorio."));
            }

            if (!title.equalsIgnoreCase(topic.getTitle()) && repository.existsTitle(topic.getUnitId(), title)) {
                throw new ValidationException(
                        List.of("Ya existe un tema con ese título dentro de la unidad.")
                );
            }
            topic.setTitle(title);
        }

        if (dto.getDescription() != null) {
            topic.setDescription(dto.getDescription());
        }

        boolean updated = repository.update(topic);

        if (!updated) {
            throw new RuntimeException("No se pudo actualizar el tema.");
        }

        return toResponseDto(topic);
    }

    public void delete(String id) {

        Topic topic = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Tema no encontrado.")
                );

        boolean deleted = repository.delete(topic.getId());

        if (!deleted) {
            throw new RuntimeException("No se pudo eliminar el tema.");
        }
    }

    private TopicResponseDto toResponseDto(Topic topic) {

        return new TopicResponseDto(
                topic.getId(),
                topic.getNumber(),
                topic.getTitle(),
                topic.getDescription()
        );
    }
}