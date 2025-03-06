package ru.samokhin.labCheck.adapter.persistence.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samokhin.labCheck.domain.teacher.Teacher;

import java.util.Optional;

public interface TeacherJpaRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByTgChatId(Long tgChatId);
    boolean existsByTgChatId(Long tgChatId);
}
