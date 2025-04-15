package ru.samokhin.labCheck.app.impl.assignmentGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.assignmentGroup.AssignmentGroupRepository;
import ru.samokhin.labCheck.app.api.assignmentGroup.CreateAssignmentGroupInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateAssignmentGroupUseCase implements CreateAssignmentGroupInbound {
    private final AssignmentGroupRepository assignmentGroupRepository;

    @Transactional
    @Override
    public boolean execute(String name) {
        try {
            Optional<AssignmentGroup> existingGroup = assignmentGroupRepository.findByNameIgnoreCase(name);
            if (existingGroup.isPresent()) {
                return false;
            }
            AssignmentGroup assignmentGroup = new AssignmentGroup(name);
            assignmentGroupRepository.save(assignmentGroup);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
