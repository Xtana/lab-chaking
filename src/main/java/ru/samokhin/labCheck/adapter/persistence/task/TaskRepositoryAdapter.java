package ru.samokhin.labCheck.adapter.persistence.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.samokhin.labCheck.app.api.task.TaskRepository;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepository {
    private final TaskJpaRepository taskJpaRepository;

    @Override
    public void save(Task task) {
        taskJpaRepository.save(task);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskJpaRepository.findById(id);
    }

    @Override
    public List<Task> findByAssignmentGroup(AssignmentGroup assignmentGroup) {
        return taskJpaRepository.findByAssignmentGroup(assignmentGroup);
    }

    @Override
    public Optional<Task> findByAssignmentGroupAndName(AssignmentGroup assignmentGroup, String name) {
        return taskJpaRepository.findByAssignmentGroupAndName(assignmentGroup, name);
    }

    @Override
    public void delete(Task task) {
        taskJpaRepository.delete(task);
    }
}
