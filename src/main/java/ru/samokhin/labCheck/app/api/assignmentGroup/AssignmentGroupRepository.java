package ru.samokhin.labCheck.app.api.assignmentGroup;

import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

import java.util.List;
import java.util.Optional;

public interface AssignmentGroupRepository {
    void save(AssignmentGroup assignmentGroup);
    Optional<AssignmentGroup> findByNameIgnoreCase(String name);
    List<AssignmentGroup> findAll();
}
