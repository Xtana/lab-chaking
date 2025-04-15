package ru.samokhin.labCheck.app.impl.assignmentGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.assignmentGroup.AssignmentGroupRepository;
import ru.samokhin.labCheck.app.api.assignmentGroup.DeleteAssignmentGroupByNameInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeleteAssignmentGroupByNameUseCase implements DeleteAssignmentGroupByNameInbound {
    private final AssignmentGroupRepository assignmentGroupRepository;

    @Transactional
    @Override
    public boolean execute(String name) {
        try {
            Optional<AssignmentGroup> existingGroup = assignmentGroupRepository.findByNameIgnoreCase(name);
            if (!existingGroup.isPresent()) {
                return false;
            }
            assignmentGroupRepository.delete(existingGroup.get());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
