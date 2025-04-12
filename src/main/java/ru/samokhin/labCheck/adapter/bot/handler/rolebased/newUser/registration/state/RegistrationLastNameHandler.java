package ru.samokhin.labCheck.adapter.bot.handler.rolebased.newUser.registration.state;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.domain.student.Student;

@Component
public class RegistrationLastNameHandler implements RegistrationStateHandler {
    @Override
    public StatusData handle(Student student, String input) {
        if (input == null || input.isBlank()) {
            return new StatusData(false, "Фамилия не может быть пустой!");
        }
        student.setLastName(input.trim());
        return new StatusData(true, "");
    }

    @Override
    public RegistrationState currantState() {
        return RegistrationState.REGISTRATION_AWAITING_LAST_NAME;
    }

    @Override
    public RegistrationState nextState() {
        return RegistrationState.REGISTRATION_AWAITING_PATRONYMIC;
    }
}
