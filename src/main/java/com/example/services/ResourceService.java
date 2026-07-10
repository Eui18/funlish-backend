package com.example.services;

import java.util.List;
import java.util.UUID;
import com.example.dtos.resource.CreateResourceDto;
import com.example.dtos.resource.ResourceResponseDto;
import com.example.dtos.resource.UpdateResourceDto;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ResourceAlreadyExistsException; // Importada
import com.example.exceptions.ValidationException;
import com.example.models.resource.Resource;
import com.example.models.resource.ResourceType;
import com.example.repository.resource.ResourceRepository;
import com.example.repository.topic.TopicRepository;

public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final TopicRepository topicRepository;

    public ResourceService(ResourceRepository resourceRepository, TopicRepository topicRepository) {
        this.resourceRepository = resourceRepository;
        this.topicRepository = topicRepository;
    }

    public ResourceResponseDto create(String topicId, CreateResourceDto dto) {
        if (topicRepository.findById(topicId).isEmpty()) {
            throw new NotFoundException("Tema no encontrado.");
        }

        dto.setName(dto.getName() != null ? dto.getName().trim() : "");

        if (resourceRepository.existsByName(topicId, dto.getName())) {
            throw new ResourceAlreadyExistsException("Ya existe un recurso con ese nombre en este tema.");
        }

        ResourceType type;
        try {
            type = ResourceType.valueOf(dto.getType().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ValidationException(List.of("Tipo de recurso no válido. Tipos aceptados: DOCUMENT, VIDEO, IMAGE, AUDIO"));
        }

        Resource resource = new Resource(
                UUID.randomUUID().toString(),
                topicId,
                dto.getName(),
                type,
                dto.getDescription(),
                dto.getUrl()
        );

        resourceRepository.create(resource);
        return toResponseDto(resource);
    }

    public ResourceResponseDto update(String id, UpdateResourceDto dto) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado."));
        if (dto.getName() != null) {
            dto.setName(dto.getName().trim());
            
            if (!resource.getName().equals(dto.getName())
                    && resourceRepository.existsByName(resource.getTopicId(), dto.getName())) {
                throw new ResourceAlreadyExistsException("Ya existe un recurso con ese nombre en este tema.");
            }
            resource.setName(dto.getName());
        }

        if (dto.getType() != null) {
            try {
                resource.setType(ResourceType.valueOf(dto.getType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ValidationException(List.of("Tipo de recurso no válido para actualizar."));
            }
        }

        if (dto.getDescription() != null) resource.setDescription(dto.getDescription());
        if (dto.getUrl() != null) resource.setUrl(dto.getUrl());

        resourceRepository.update(resource);
        return toResponseDto(resource);
    }

    public List<ResourceResponseDto> findAll(String topicId) {
        if (topicRepository.findById(topicId).isEmpty()) {
            throw new NotFoundException("Tema no encontrado.");
        }
        return resourceRepository.findAll(topicId).stream().map(this::toResponseDto).toList();
    }

    public ResourceResponseDto findById(String id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado."));
        return toResponseDto(resource);
    }

    public void delete(String id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado."));
        resourceRepository.delete(resource.getId());
    }

    private ResourceResponseDto toResponseDto(Resource resource) {
        return new ResourceResponseDto(
                resource.getId(),
                resource.getName(),
                resource.getType().name(),
                resource.getDescription(),
                resource.getUrl()
        );
    }
}