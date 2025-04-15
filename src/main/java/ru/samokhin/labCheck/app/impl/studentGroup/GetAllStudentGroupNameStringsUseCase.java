package ru.samokhin.labCheck.app.impl.studentGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.studentGroup.GetAllStudentGroupNameStringsInbound;
import ru.samokhin.labCheck.app.api.studentGroup.StudentGroupRepository;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllStudentGroupNameStringsUseCase implements GetAllStudentGroupNameStringsInbound {
    private final StudentGroupRepository studentGroupRepository;

    @Transactional
    @Override
    public List<String> execute() {
        return studentGroupRepository.findAll().stream()
                .map(StudentGroup::getName)
                .toList();
    }
}
