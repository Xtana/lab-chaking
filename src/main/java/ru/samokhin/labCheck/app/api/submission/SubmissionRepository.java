package ru.samokhin.labCheck.app.api.submission;

import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.submission.Submission;
import ru.samokhin.labCheck.domain.task.Task;

public interface SubmissionRepository {
    void save(Submission submission);
    void deleteByStudentAndTask(Student student, Task task) ;
}
