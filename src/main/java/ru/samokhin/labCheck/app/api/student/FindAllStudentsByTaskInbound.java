package ru.samokhin.labCheck.app.api.student;

import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.List;

public interface FindAllStudentsByTaskInbound {
    List<Student> execute(Task task);
}
