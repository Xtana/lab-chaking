package ru.samokhin.labCheck.app.impl.studentGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.studentGroup.CreateStudentGroupInbound;
import ru.samokhin.labCheck.app.api.studentGroup.StudentGroupRepository;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

@Component
@RequiredArgsConstructor
public class CreateStudentGroupUseCase implements CreateStudentGroupInbound {
    private final StudentGroupRepository studentGroupRepository;

    @Transactional
    @Override
    public boolean execute(String name) {
        try {
            StudentGroup studentGroup = new StudentGroup(name);
            studentGroupRepository.save(studentGroup);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
