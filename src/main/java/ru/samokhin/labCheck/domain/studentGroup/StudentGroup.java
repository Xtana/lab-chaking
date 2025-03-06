package ru.samokhin.labCheck.domain.studentGroup;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "STUDENT_GROUP")
@RequiredArgsConstructor
@Getter
@Setter
public class StudentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
}
