package ru.samokhin.labCheck.adapter.persistence.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.samokhin.labCheck.app.api.teacher.TeacherRepository;
import ru.samokhin.labCheck.domain.teacher.Teacher;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeacherRepositoryAdapter implements TeacherRepository {
    private final TeacherJpaRepository teacherJpaRepository;

    @Override
    public void save(Teacher teacher) {
        teacherJpaRepository.save(teacher);
    }

    @Override
    public Optional<Teacher> findByTgChatId(Long tgChatId) {
        return teacherJpaRepository.findByTgChatId(tgChatId);
    }

    @Override
    public boolean existsByTgChatId(Long tgChatId) {
        return teacherJpaRepository.existsByTgChatId(tgChatId);
    }


}
