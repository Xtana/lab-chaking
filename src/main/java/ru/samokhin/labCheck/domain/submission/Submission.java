package ru.samokhin.labCheck.domain.submission;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.task.Task;

@Entity
@Table(
        name = "SUBMISSION",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "task_id"})
)
@NoArgsConstructor
@Getter
@Setter
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String script;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}
