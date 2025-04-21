package ru.samokhin.labCheck.adapter.persistence.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.samokhin.labCheck.app.api.student.StudentRepository;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.studentGroup.StudentPassedTestsDto;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryAdapter implements StudentRepository {
    private final StudentJpaRepository studentJpaRepository;

    @Override
    public void save(Student student) {
        studentJpaRepository.save(student);
    }

    @Override
    public Optional<Student> findByTgChatId(Long tgChatId) {
        return studentJpaRepository.findByTgChatId(tgChatId);
    }

    @Override
    public List<Student> findAllByTask(Task task) {
        return studentJpaRepository.findAllByTask(task);
    }

    @Override
    public List<StudentPassedTestsDto> findStudentsWithPassedTestCountByTask(Task task) {
        return studentJpaRepository.findStudentsWithPassedTestCountByTask(task);
    }

    @Override
    public boolean existsByTgChatId(Long tgChatId) {
        return studentJpaRepository.existsByTgChatId(tgChatId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return studentJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByStudentCardNumber(String studentCardNumber) {
        return studentJpaRepository.existsByStudentCardNumber(studentCardNumber);
    }


}
