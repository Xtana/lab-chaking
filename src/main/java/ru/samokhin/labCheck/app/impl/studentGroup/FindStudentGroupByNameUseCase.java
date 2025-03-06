package ru.samokhin.labCheck.app.impl.studentGroup;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.studentGroup.FindStudentGroupByNameInbound;
import ru.samokhin.labCheck.app.api.studentGroup.StudentGroupRepository;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

@Component
@RequiredArgsConstructor
public class FindStudentGroupByNameUseCase implements FindStudentGroupByNameInbound {
    private final StudentGroupRepository studentGroupRepository;

    @Transactional(readOnly = true)
    @Override
    public StudentGroup execute(String name) {
        return studentGroupRepository.findByNameIgnoreCase(name.trim())
                .orElseThrow(() -> new EntityNotFoundException("Student group not found with -> student group name: " + name));
    }
}
