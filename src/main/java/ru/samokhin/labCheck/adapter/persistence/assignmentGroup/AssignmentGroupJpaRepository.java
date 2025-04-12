package ru.samokhin.labCheck.adapter.persistence.assignmentGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

import java.util.Optional;

public interface AssignmentGroupJpaRepository extends JpaRepository<AssignmentGroup, Long> {
    Optional<AssignmentGroup> findByNameIgnoreCase(String name);
}
