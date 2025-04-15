package ru.samokhin.labCheck.adapter.persistence.task;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.List;
import java.util.Optional;

public interface TaskJpaRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignmentGroup(AssignmentGroup assignmentGroup);
    Optional<Task> findByAssignmentGroupAndName(AssignmentGroup assignmentGroup, String name);
}
