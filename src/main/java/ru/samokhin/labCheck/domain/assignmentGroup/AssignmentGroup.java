package ru.samokhin.labCheck.domain.assignmentGroup;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ASSIGNMENT_GROUP")
@RequiredArgsConstructor
@Getter
@Setter
public class AssignmentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public AssignmentGroup(String name) {
        this.name = name;
    }
}
