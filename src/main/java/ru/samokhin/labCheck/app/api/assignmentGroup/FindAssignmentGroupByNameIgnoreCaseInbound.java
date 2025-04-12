package ru.samokhin.labCheck.app.api.assignmentGroup;

import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

public interface FindAssignmentGroupByNameIgnoreCaseInbound {
    AssignmentGroup execute(String name);
}
