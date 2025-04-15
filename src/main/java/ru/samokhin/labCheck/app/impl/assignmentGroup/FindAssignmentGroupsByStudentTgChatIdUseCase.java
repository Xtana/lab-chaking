package ru.samokhin.labCheck.app.impl.assignmentGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.assignmentGroup.AssignmentGroupRepository;
import ru.samokhin.labCheck.app.api.assignmentGroup.FindAssignmentGroupsByStudentTgChatIdInbound;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindAssignmentGroupsByStudentTgChatIdUseCase implements
        FindAssignmentGroupsByStudentTgChatIdInbound {
    private final AssignmentGroupRepository assignmentGroupRepository;

    @Transactional(readOnly = true)
    @Override
    public List<String> execute(Long tgChatId) {
        return assignmentGroupRepository.findByStudentTgChatId(tgChatId);
    }
}
