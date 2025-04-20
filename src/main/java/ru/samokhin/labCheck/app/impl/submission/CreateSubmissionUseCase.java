package ru.samokhin.labCheck.app.impl.submission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.submission.CreateSubmissionInbound;
import ru.samokhin.labCheck.app.api.submission.SubmissionRepository;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.submission.Submission;
import ru.samokhin.labCheck.domain.task.Task;

@Component
@RequiredArgsConstructor
public class CreateSubmissionUseCase implements CreateSubmissionInbound {
    private final SubmissionRepository submissionRepository;

    @Transactional
    @Override
    public Submission execute(String script, Task task, Student student) {
        Submission submission = new Submission(
                script,
                task,
                student
        );
        try {
            submissionRepository.save(submission);
        } catch (Exception e) {
            throw e;
        }
        return submission;
    }
}
