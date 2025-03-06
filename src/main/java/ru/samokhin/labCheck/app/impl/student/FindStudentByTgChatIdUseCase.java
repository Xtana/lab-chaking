package ru.samokhin.labCheck.app.impl.student;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.app.api.student.FindStudentByTgChatIdInbound;
import ru.samokhin.labCheck.app.api.student.StudentRepository;
import ru.samokhin.labCheck.domain.student.Student;

@Component
@RequiredArgsConstructor
public class FindStudentByTgChatIdUseCase implements FindStudentByTgChatIdInbound {
    private final StudentRepository studentRepository;

    @Override
    public Student execute(Long tgChatId) {
        return studentRepository.findByTgChatId(tgChatId)
                .orElseThrow(() -> new EntityNotFoundException("Student Not Found with -> tg chat id: " + tgChatId));
    }
}
