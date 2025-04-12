package ru.samokhin.labCheck.adapter.bot.handler.rolebased.newUser.registration.state;

import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.domain.student.Student;

public interface RegistrationStateHandler {
    StatusData handle(Student student, String input);
    RegistrationState currantState();
    RegistrationState nextState();
}
