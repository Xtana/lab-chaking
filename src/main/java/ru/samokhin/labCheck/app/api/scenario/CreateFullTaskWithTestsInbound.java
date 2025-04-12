package ru.samokhin.labCheck.app.api.scenario;

import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;
import ru.samokhin.labCheck.domain.teacher.Teacher;
import ru.samokhin.labCheck.domain.test.Test;

import java.util.Set;

public interface CreateFullTaskWithTestsInbound {
    boolean execute(
            String name,
            String description,
            Teacher teacher,
            AssignmentGroup assignmentGroup,
            Set<StudentGroup> studentGroups,
            Set<Test> tests
    );
}
