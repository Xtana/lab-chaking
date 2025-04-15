package ru.samokhin.labCheck.app.api.task;

import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.task.Task;

public interface FindTaskByAssignmentGroupAndNameInbound {
    Task execute(AssignmentGroup assignmentGroup, String name);
}
