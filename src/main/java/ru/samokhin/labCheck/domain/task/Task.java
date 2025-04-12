package ru.samokhin.labCheck.domain.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;
import ru.samokhin.labCheck.domain.teacher.Teacher;

import java.util.Set;

@Entity
@Table(name = "TASK")
@NoArgsConstructor
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_group_id", nullable = false)
    private AssignmentGroup assignmentGroup;

    @ManyToMany
    @JoinTable(
            name = "TASK_STUDENT_GROUP",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "student_group_id")
    )
    private Set<StudentGroup> studentGroups;

    public Task(String name, String description,
                Teacher teacher, AssignmentGroup assignmentGroup,
                Set<StudentGroup> studentGroups) {
        this.name = name;
        this.description = description;
        this.teacher = teacher;
        this.assignmentGroup = assignmentGroup;
        this.studentGroups = studentGroups;
    }
}


