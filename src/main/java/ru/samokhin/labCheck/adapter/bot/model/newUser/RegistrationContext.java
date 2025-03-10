package ru.samokhin.labCheck.adapter.bot.model.newUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.samokhin.labCheck.domain.student.Student;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationContext {
    private Student student;
    private RegistrationState currentState;
}

