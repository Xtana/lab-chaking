package ru.samokhin.labCheck.app.impl.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.app.api.student.IsStudentExistsByStudentCardNumberInbound;
import ru.samokhin.labCheck.app.api.student.StudentRepository;

@Component
@RequiredArgsConstructor
public class IsStudentExistsByStudentCardNumberUseCase implements IsStudentExistsByStudentCardNumberInbound {
    private final StudentRepository studentRepository;

    @Override
    public boolean execute(String studentCardNumber) {
        return studentRepository.existsByStudentCardNumber(studentCardNumber);
    }
}
