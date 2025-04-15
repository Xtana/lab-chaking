package ru.samokhin.labCheck.app.impl.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.teacher.IsTeacherExistsByTgChatIdInbound;
import ru.samokhin.labCheck.app.api.teacher.TeacherRepository;

@Component
@RequiredArgsConstructor
public class IsTeacherExistsByTgChatIdUseCase implements IsTeacherExistsByTgChatIdInbound {
    private final TeacherRepository teacherRepository;

    @Transactional(readOnly = true)
    @Override
    public boolean execute(Long tgChatId) {
        return teacherRepository.existsByTgChatId(tgChatId);
    }
}
