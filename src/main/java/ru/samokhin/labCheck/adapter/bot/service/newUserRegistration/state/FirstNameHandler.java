package ru.samokhin.labCheck.adapter.bot.service.newUserRegistration.state;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationStatusData;
import ru.samokhin.labCheck.domain.student.Student;

@Component
public class FirstNameHandler implements RegistrationStateHandler {
    @Override
    public RegistrationStatusData handle(Student student, String input) {
        if (input == null || input.isBlank()) {
            return new RegistrationStatusData(false, "Имя не может быть пустым!");
        }
        student.setFirstName(input.trim());
        return new RegistrationStatusData(true, "");
    }

    @Override
    public RegistrationState currantState() {
        return RegistrationState.AWAITING_FIRST_NAME;
    }

    @Override
    public RegistrationState nextState() {
        return RegistrationState.AWAITING_LAST_NAME;
    }
}

