package ru.samokhin.labCheck.domain.studentGroup;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "STUDENT_GROUP")
@Getter
@Setter
@RequiredArgsConstructor
public class StudentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public StudentGroup(String name) {
        this.name = name;
    }
}
