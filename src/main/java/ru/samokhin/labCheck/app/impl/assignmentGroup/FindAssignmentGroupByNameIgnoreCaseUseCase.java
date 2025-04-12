package ru.samokhin.labCheck.app.impl.assignmentGroup;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.app.api.assignmentGroup.AssignmentGroupRepository;
import ru.samokhin.labCheck.app.api.assignmentGroup.FindAssignmentGroupByNameIgnoreCaseInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

@Component
@RequiredArgsConstructor
public class FindAssignmentGroupByNameIgnoreCaseUseCase implements
        FindAssignmentGroupByNameIgnoreCaseInbound {
    private final AssignmentGroupRepository assignmentGroupRepository;

    @Override
    public AssignmentGroup execute(String name) {
        return assignmentGroupRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException("Assignment group Not Found with -> name: " + name));
    }
}
