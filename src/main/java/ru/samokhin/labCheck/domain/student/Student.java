package ru.samokhin.labCheck.domain.student;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

@Entity
@Table(name = "STUDENT")
@NoArgsConstructor
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String patronymic;

    @Column(nullable = false)
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_group_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StudentGroup studentGroup;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String studentCardNumber;

    @Column(unique = true, nullable = false)
    private Long tgChatId;

    public Student(String firstName, String patronymic, String lastName,
                   StudentGroup studentGroup, String email, String studentCardNumber, Long tgChatId) {
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.lastName = lastName;
        this.studentGroup = studentGroup;
        this.email = email;
        this.studentCardNumber = studentCardNumber;
        this.tgChatId = tgChatId;
    }
}
