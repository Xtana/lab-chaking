package ru.samokhin.labCheck.adapter.persistence.submission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.submission.Submission;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.Optional;

public interface SubmissionJpaRepository extends JpaRepository<Submission, Long> {
    void deleteByStudentAndTask(Student student, Task task);
}
