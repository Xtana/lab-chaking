package ru.samokhin.labCheck.app.impl.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.app.api.student.IsStudentExistsByEmailInbound;
import ru.samokhin.labCheck.app.api.student.StudentRepository;

@Component
@RequiredArgsConstructor
public class IsStudentExistsByEmailUseCase implements IsStudentExistsByEmailInbound {
    private final StudentRepository studentRepository;

    @Override
    public boolean execute(String email) {
        return studentRepository.existsByEmail(email);
    }
}
