package ru.samokhin.labCheck.app.impl.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.task.FindTaskByAssignmentGroupInbound;
import ru.samokhin.labCheck.app.api.task.TaskRepository;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindTaskByAssignmentGroupUseCase implements FindTaskByAssignmentGroupInbound {
    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Task> execute(AssignmentGroup assignmentGroup) {
        return taskRepository.findByAssignmentGroup(assignmentGroup);
    }
}
