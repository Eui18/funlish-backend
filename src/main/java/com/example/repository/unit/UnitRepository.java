package com.example.repository.unit;

import java.util.List;
import java.util.Optional;
import com.example.models.unit.Unit;

public interface UnitRepository {

    List<Unit> findAll(String groupId);
    Optional<Unit> findById(String id);
    Unit create(Unit unit);
    boolean update(Unit unit);
    boolean delete(String id);
    Optional<Unit> findByName(String name, String groupId);
    int nextNumber(String groupId);
}