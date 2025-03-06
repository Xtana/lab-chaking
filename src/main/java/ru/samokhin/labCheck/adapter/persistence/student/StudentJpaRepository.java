package ru.samokhin.labCheck.adapter.persistence.student;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samokhin.labCheck.domain.student.Student;

import java.util.Optional;

public interface StudentJpaRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByTgChatId(Long tgChatId);
    boolean existsByTgChatId(Long tgChatId);
}
