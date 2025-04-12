package ru.samokhin.labCheck.domain.teacher;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TEACHER")
@RequiredArgsConstructor
@Getter
@Setter
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String patronymic;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private Long tgChatId;

    public Teacher(String firstName, String patronymic, String lastName,
                   String email, Long tgChatId) {
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.lastName = lastName;
        this.email = email;
        this.tgChatId = tgChatId;
    }
}

