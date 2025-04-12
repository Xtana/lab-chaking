package ru.samokhin.labCheck.app.impl.assignmentGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.app.api.assignmentGroup.AssignmentGroupRepository;
import ru.samokhin.labCheck.app.api.assignmentGroup.CreateAssignmentGroupInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

@Component
@RequiredArgsConstructor
public class CreateAssignmentGroupUseCase implements CreateAssignmentGroupInbound {
    private final AssignmentGroupRepository assignmentGroupRepository;

    @Override
    public boolean execute(String name) {
        try {
            AssignmentGroup assignmentGroup = new AssignmentGroup(
                    name
            );
            // TODO добавить проверку на уникальность name
            assignmentGroupRepository.save(assignmentGroup);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
