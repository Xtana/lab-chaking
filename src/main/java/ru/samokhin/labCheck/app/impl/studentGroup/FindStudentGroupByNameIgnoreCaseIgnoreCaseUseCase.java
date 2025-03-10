package ru.samokhin.labCheck.app.impl.studentGroup;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.studentGroup.FindStudentGroupByNameIgnoreCaseInbound;
import ru.samokhin.labCheck.app.api.studentGroup.StudentGroupRepository;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

@Component
@RequiredArgsConstructor
public class FindStudentGroupByNameIgnoreCaseIgnoreCaseUseCase implements FindStudentGroupByNameIgnoreCaseInbound {
    private final StudentGroupRepository studentGroupRepository;

    @Transactional(readOnly = true)
    @Override
    public StudentGroup execute(String name) {
        return studentGroupRepository.findByNameIgnoreCase(name.trim())
                .orElseThrow(() -> new EntityNotFoundException("Student group not found with -> student group name: " + name));
    }
}
