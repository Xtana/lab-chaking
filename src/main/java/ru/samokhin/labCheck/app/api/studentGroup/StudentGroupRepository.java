package ru.samokhin.labCheck.app.api.studentGroup;

import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

import java.util.List;
import java.util.Optional;

public interface StudentGroupRepository {
    List<StudentGroup> findAll();
    void save(StudentGroup studentGroup);
    Optional<StudentGroup> findByNameIgnoreCase(String name);
    void delete(StudentGroup studentGroup);
}
