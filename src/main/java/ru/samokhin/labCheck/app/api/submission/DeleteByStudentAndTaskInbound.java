package ru.samokhin.labCheck.app.api.submission;

import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.task.Task;

public interface DeleteByStudentAndTaskInbound {
    void execute(Student student, Task task);
}
