package ru.samokhin.labCheck.adapter.persistence.submission;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samokhin.labCheck.domain.submission.Submission;

public interface SubmissionJpaRepository extends JpaRepository<Submission, Long> {
}
