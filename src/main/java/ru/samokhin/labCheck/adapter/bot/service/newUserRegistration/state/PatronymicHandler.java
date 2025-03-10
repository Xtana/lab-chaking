package ru.samokhin.labCheck.adapter.bot.service.newUserRegistration.state;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationStatusData;
import ru.samokhin.labCheck.domain.student.Student;

@Component
public class PatronymicHandler implements RegistrationStateHandler {
    @Override
    public RegistrationStatusData handle(Student student, String input) {
        if (input == null || input.isBlank()) {
            return new RegistrationStatusData(false, "Отчество не может быть пустым!");
        }
        if (input.equals("-")) {
            student.setPatronymic(null);
        } else {
            student.setPatronymic(input.trim());
        }
        return new RegistrationStatusData(true, "");
    }

    @Override
    public RegistrationState currantState() {
        return RegistrationState.AWAITING_PATRONYMIC;
    }

    @Override
    public RegistrationState nextState() {
        return RegistrationState.AWAITING_STUDENT_CARD_NUMBER;
    }
}