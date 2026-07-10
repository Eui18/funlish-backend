package com.example.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.dtos.unit.CreateUnitDto;
import com.example.dtos.unit.UnitResponseDto;
import com.example.dtos.unit.UpdateUnitDto;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ResourceAlreadyExistsException;
import com.example.exceptions.ValidationException;
import com.example.models.group.Group;
import com.example.models.unit.Unit;
import com.example.repository.group.GroupRepository;
import com.example.repository.unit.UnitRepository;

public class UnitService {

    private final UnitRepository repository;
    private final GroupRepository groupRepository;

    public UnitService(UnitRepository repository, GroupRepository groupRepository) {
        this.repository = repository;
        this.groupRepository = groupRepository;
    }

    public List<UnitResponseDto> findAll(String groupId) {
        groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Grupo no encontrado."));

        List<Unit> units = repository.findAll(groupId);
        List<UnitResponseDto> response = new ArrayList<>();

        for (Unit unit : units) {
            response.add(toResponseDto(unit));
        }

        return response;
    }

    public UnitResponseDto findById(String id) {
        Unit unit = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unidad no encontrada."));

        return toResponseDto(unit);
    }

    public UnitResponseDto create(String groupId, CreateUnitDto dto) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Grupo no encontrado."));

        String cleanedName = dto.getName() != null ? dto.getName().trim() : "";

        if (cleanedName.isBlank()) {
            throw new ValidationException(List.of("El nombre de la unidad es obligatorio."));
        }

        Optional<Unit> optional = repository.findByName(cleanedName, group.getId());

        if (optional.isPresent()) {
            throw new ResourceAlreadyExistsException("Ya existe una unidad con ese nombre en este grupo.");
        }

        int number = repository.nextNumber(group.getId());

        Unit unit = new Unit(
                UUID.randomUUID().toString(),
                group.getId(),
                cleanedName,
                number
        );

        repository.create(unit);

        return toResponseDto(unit);
    }

    public UnitResponseDto update(String id, UpdateUnitDto dto) {
        Unit unit = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unidad no encontrada."));

        if (dto.getName() != null) {
            String cleanedName = dto.getName().trim();

            if (cleanedName.isBlank()) {
                throw new ValidationException(List.of("El nombre de la unidad es obligatorio."));
            }

            Optional<Unit> optional = repository.findByName(cleanedName, unit.getGroupId());

            if (optional.isPresent() && !optional.get().getId().equals(unit.getId())) {
                throw new ResourceAlreadyExistsException("Ya existe una unidad con ese nombre en este grupo.");
            }

            unit.setName(cleanedName);
        }

        boolean updated = repository.update(unit);

        if (!updated) {
            throw new RuntimeException("No se pudo actualizar la unidad.");
        }

        return toResponseDto(unit);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unidad no encontrada."));

        boolean deleted = repository.delete(id);

        if (!deleted) {
            throw new RuntimeException("No se pudo eliminar la unidad.");
        }
    }

    private UnitResponseDto toResponseDto(Unit unit) {
        return new UnitResponseDto(
                unit.getId(),
                unit.getName(),
                unit.getNumber()
        );
    }
}