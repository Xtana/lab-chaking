package ru.samokhin.labCheck.app.impl.studentGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.studentGroup.DeleteStudentGroupInbound;
import ru.samokhin.labCheck.app.api.studentGroup.StudentGroupRepository;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeleteStudentGroupUseCase implements DeleteStudentGroupInbound {
    private final StudentGroupRepository studentGroupRepository;

    @Transactional
    @Override
    public boolean execute(String name) {
        try {
            Optional<StudentGroup> existingGroup = studentGroupRepository.findByNameIgnoreCase(name);
            if (!existingGroup.isPresent()) {
                return false;
            }
            studentGroupRepository.delete(existingGroup.get());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
