package ru.samokhin.labCheck.app.impl.submission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.submission.DeleteByStudentAndTaskInbound;
import ru.samokhin.labCheck.app.api.submission.SubmissionRepository;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.task.Task;


@Component
@RequiredArgsConstructor
public class DeleteByStudentAndTaskUseCase implements DeleteByStudentAndTaskInbound {
    private final SubmissionRepository submissionRepository;

    @Transactional
    @Override
    public void execute(Student student, Task task) {
        submissionRepository.deleteByStudentAndTask(student, task);
    }
}
