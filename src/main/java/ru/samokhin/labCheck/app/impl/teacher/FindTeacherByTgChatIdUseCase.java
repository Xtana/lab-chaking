package ru.samokhin.labCheck.app.impl.teacher;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.app.api.teacher.FindTeacherByTgChatIdInbound;
import ru.samokhin.labCheck.app.api.teacher.TeacherRepository;
import ru.samokhin.labCheck.domain.teacher.Teacher;

@Component
@RequiredArgsConstructor
public class FindTeacherByTgChatIdUseCase implements FindTeacherByTgChatIdInbound {
    private final TeacherRepository teacherRepository;


    @Override
    public Teacher execute(Long tgChatId) {
        return teacherRepository.findByTgChatId(tgChatId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher Not Found with -> tg chat id: " + tgChatId));
    }
}
