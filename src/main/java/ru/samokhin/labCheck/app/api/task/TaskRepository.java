package ru.samokhin.labCheck.app.api.task;

import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    void save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findByAssignmentGroup(AssignmentGroup assignmentGroup);
    Optional<Task> findByAssignmentGroupAndName(AssignmentGroup assignmentGroup, String name);
    void delete(Task task);
}