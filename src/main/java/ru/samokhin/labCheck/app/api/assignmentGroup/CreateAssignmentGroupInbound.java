package ru.samokhin.labCheck.app.api.assignmentGroup;

import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.teacher.Teacher;

import java.util.Set;

public interface CreateAssignmentGroupInbound {
    boolean execute(String name, String description, Teacher teacher, Set<Student> students);
}
