package ru.samokhin.labCheck.adapter.persistence.studentGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.samokhin.labCheck.app.api.studentGroup.StudentGroupRepository;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentGroupRepositoryAdapter implements StudentGroupRepository {
    private final StudentGroupJpaRepository studentGroupJpaRepository;

    @Override
    public List<StudentGroup> findAll() {
        return studentGroupJpaRepository.findAll();
    }

    @Override
    public void save(StudentGroup studentGroup) {
        studentGroupJpaRepository.save(studentGroup);
    }

    @Override
    public Optional<StudentGroup> findByNameIgnoreCase(String name) {
        return studentGroupJpaRepository.findByNameIgnoreCase(name);
    }

    @Override
    public void delete(StudentGroup studentGroup) {
        studentGroupJpaRepository.delete(studentGroup);
    }
}
