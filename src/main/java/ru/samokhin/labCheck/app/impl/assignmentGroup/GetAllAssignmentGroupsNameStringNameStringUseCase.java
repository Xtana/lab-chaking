package ru.samokhin.labCheck.app.impl.assignmentGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.app.api.assignmentGroup.AssignmentGroupRepository;
import ru.samokhin.labCheck.app.api.assignmentGroup.GetAllAssignmentGroupsNameStringInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllAssignmentGroupsNameStringNameStringUseCase implements GetAllAssignmentGroupsNameStringInbound {
    private final AssignmentGroupRepository assignmentGroupRepository;

    @Override
    public List<String> execute() {
        return assignmentGroupRepository.findAll().stream()
                .map(AssignmentGroup::getName)
                .toList();
    }
}
