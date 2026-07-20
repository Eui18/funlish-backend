package com.example.utils;

import java.util.List;

import com.example.exceptions.NotFoundException;
import com.example.exceptions.ValidationException;
import com.example.models.group.Group;
import com.example.models.topic.Topic;
import com.example.models.unit.Unit;
import com.example.repository.group.GroupRepository;
import com.example.repository.topic.TopicRepository;
import com.example.repository.unit.UnitRepository;

public class OwnershipValidator {

    private final GroupRepository groupRepository;
    private final UnitRepository unitRepository;
    private final TopicRepository topicRepository;

    public OwnershipValidator(
            GroupRepository groupRepository,
            UnitRepository unitRepository,
            TopicRepository topicRepository) {

        this.groupRepository = groupRepository;
        this.unitRepository = unitRepository;
        this.topicRepository = topicRepository;
    }

    public void assertTeacherOwnsGroup(String groupId, String teacherId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Grupo no encontrado."));

        if (!group.getTeacherId().equals(teacherId)) {
            throw new ValidationException(
                    List.of("No tienes permiso sobre este grupo.")
            );
        }
    }

    public void assertTeacherOwnsUnit(String unitId, String teacherId) {

        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new NotFoundException("Unidad no encontrada."));

        assertTeacherOwnsGroup(unit.getGroupId(), teacherId);
    }

    public void assertTeacherOwnsTopic(String topicId, String teacherId) {

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Tema no encontrado."));

        assertTeacherOwnsUnit(topic.getUnitId(), teacherId);
    }
}
