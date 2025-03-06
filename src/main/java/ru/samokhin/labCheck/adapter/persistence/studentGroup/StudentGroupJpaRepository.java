package ru.samokhin.labCheck.adapter.persistence.studentGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

import java.util.Optional;

public interface StudentGroupJpaRepository extends JpaRepository<StudentGroup, Long> {
    Optional<StudentGroup> findByNameIgnoreCase(String name);
}
