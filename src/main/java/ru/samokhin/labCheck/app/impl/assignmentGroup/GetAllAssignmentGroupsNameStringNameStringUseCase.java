package ru.samokhin.labCheck.app.impl.assignmentGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.assignmentGroup.AssignmentGroupRepository;
import ru.samokhin.labCheck.app.api.assignmentGroup.GetAllAssignmentGroupsNameStringInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllAssignmentGroupsNameStringNameStringUseCase implements GetAllAssignmentGroupsNameStringInbound {
    private final AssignmentGroupRepository assignmentGroupRepository;

    @Transactional(readOnly = true)
    @Override
    public List<String> execute() {
        return assignmentGroupRepository.findAll().stream()
                .map(AssignmentGroup::getName)
                .toList();
    }
}
