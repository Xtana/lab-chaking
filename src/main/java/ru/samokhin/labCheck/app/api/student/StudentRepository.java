package ru.samokhin.labCheck.app.api.student;

import ru.samokhin.labCheck.domain.student.Student;
import java.util.Optional;

public interface StudentRepository {
    void save(Student student);

    Optional<Student> findByTgChatId(Long tgChatId);

    boolean existsByTgChatId(Long tgChatId);
}
