package ru.samokhin.labCheck.adapter.persistence.assignmentGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

public interface AssignmentGroupJpaRepository extends JpaRepository<AssignmentGroup, Long> {
}
