package ru.samokhin.labCheck.adapter.bot.service.newUserRegistration.state;

import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationStatusData;
import ru.samokhin.labCheck.domain.student.Student;

public interface RegistrationStateHandler {
    RegistrationStatusData handle(Student student, String input);
    RegistrationState currantState();
    RegistrationState nextState();
}
