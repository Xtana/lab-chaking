package ru.samokhin.labCheck.app.api.student;

import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.studentGroup.StudentPassedTestsDto;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    void save(Student student);
    Optional<Student> findByTgChatId(Long tgChatId);
    List<Student> findAllByTask(Task task);
    List<StudentPassedTestsDto> findStudentsWithPassedTestCountByTask(Task task);
    boolean existsByTgChatId(Long tgChatId);
    boolean existsByEmail(String email);
    boolean existsByStudentCardNumber(String studentCardNumber);
}
