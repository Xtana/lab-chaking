package ru.samokhin.labCheck.app.api.task;

import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;
import ru.samokhin.labCheck.domain.teacher.Teacher;

import java.util.Set;

public interface CreateTaskInbound {
    boolean execute(String name, String description, Teacher teacher,
                    AssignmentGroup assignmentGroup, Set<StudentGroup> studentGroups);
}
