package ru.samokhin.labCheck.app.api.submission;

import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.submission.Submission;
import ru.samokhin.labCheck.domain.task.Task;

public interface CreateSubmissionInbound {
    Submission execute(String script, Task task, Student student);
}
