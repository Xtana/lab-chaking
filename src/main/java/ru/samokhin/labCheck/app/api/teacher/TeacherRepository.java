package ru.samokhin.labCheck.app.api.teacher;

import ru.samokhin.labCheck.domain.teacher.Teacher;
import java.util.Optional;

public interface TeacherRepository {
    void save(Teacher teacher);
    Optional<Teacher> findByTgChatId(Long tgChatId);
    boolean existsByTgChatId(Long tgChatId);
}
