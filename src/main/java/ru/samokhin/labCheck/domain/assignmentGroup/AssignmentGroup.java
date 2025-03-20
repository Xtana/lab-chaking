package ru.samokhin.labCheck.domain.assignmentGroup;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.teacher.Teacher;

import java.util.Set;

@Entity
@Table(name = "ASSIGNMENT_GROUP")
@Getter
@Setter
public class AssignmentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToMany
    @JoinTable(
            name = "ASSIGNMENT_GROUP_STUDENT",
            joinColumns = @JoinColumn(name = "assignment_group_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> students;

    public AssignmentGroup(String name, String description,
                           Teacher teacher, Set<Student> students) {
        this.name = name;
        this.description = description;
        this.teacher = teacher;
        this.students = students;
    }
}


