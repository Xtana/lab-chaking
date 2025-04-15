package ru.samokhin.labCheck.app.impl.task;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.task.FindTaskByAssignmentGroupAndNameInbound;
import ru.samokhin.labCheck.app.api.task.TaskRepository;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.task.Task;

@Component
@RequiredArgsConstructor
public class FindTaskByAssignmentGroupAndNameUseCase implements FindTaskByAssignmentGroupAndNameInbound {
    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    @Override
    public Task execute(AssignmentGroup assignmentGroup, String name) {
        return taskRepository.findByAssignmentGroupAndName(assignmentGroup, name.trim())
                .orElseThrow(() -> new EntityNotFoundException("Task not found with -> task name: " + name + " and assignment group: " + assignmentGroup.toString()));
    }
}
