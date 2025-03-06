package ru.samokhin.labCheck.app.impl.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.app.api.student.IsStudentExistsByTgChatIdInbound;
import ru.samokhin.labCheck.app.api.student.StudentRepository;

@Component
@RequiredArgsConstructor
public class IsStudentExistsByTgChatIdUseCase implements IsStudentExistsByTgChatIdInbound {
    private final StudentRepository studentRepository;

    @Override
    public boolean execute(Long tgChatId) {
        return studentRepository.existsByTgChatId(tgChatId);
    }
}
