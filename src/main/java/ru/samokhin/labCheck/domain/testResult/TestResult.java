package ru.samokhin.labCheck.domain.testResult;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.samokhin.labCheck.domain.submission.Submission;
import ru.samokhin.labCheck.domain.test.Test;

@Entity
@Table(
        name = "TEST_RESULT",
        uniqueConstraints = @UniqueConstraint(columnNames = {"test_id", "submission_id"})
)
@NoArgsConstructor
@Getter
@Setter
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String log;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTestResult status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    public TestResult(String log, StatusTestResult status, Test test, Submission submission) {
        this.log = log;
        this.status = status;
        this.test = test;
        this.submission = submission;
    }
}

