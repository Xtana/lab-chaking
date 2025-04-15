package ru.samokhin.labCheck.app.api.task;

import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.List;

public interface FindTaskByAssignmentGroupInbound {
    List<Task> execute(AssignmentGroup assignmentGroup);
}
