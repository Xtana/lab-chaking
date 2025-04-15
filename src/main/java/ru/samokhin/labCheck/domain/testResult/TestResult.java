package ru.samokhin.labCheck.domain.testResult;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "RESULT",
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
    private ru.samokhin.labCheck.domain.test.Test test;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private ru.samokhin.labCheck.domain.submission.Submission submission;
}

