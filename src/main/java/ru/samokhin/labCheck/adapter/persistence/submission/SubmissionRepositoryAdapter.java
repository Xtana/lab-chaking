package ru.samokhin.labCheck.adapter.persistence.submission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.submission.SubmissionRepository;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.submission.Submission;
import ru.samokhin.labCheck.domain.task.Task;

@Repository
@RequiredArgsConstructor
public class SubmissionRepositoryAdapter implements SubmissionRepository {
    private final SubmissionJpaRepository submissionJpaRepository;

    @Transactional
    @Override
    public void save(Submission submission) {
        submissionJpaRepository.save(submission);
    }

    @Transactional
    @Override
    public void deleteByStudentAndTask(Student student, Task task) {
        submissionJpaRepository.deleteByStudentAndTask(student, task);
        submissionJpaRepository.flush();
    }
}
