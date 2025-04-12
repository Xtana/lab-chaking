package ru.samokhin.labCheck.adapter.bot.handler.rolebased.newUser.registration.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.app.api.student.IsStudentExistsByStudentCardNumberInbound;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.staging.validationUtils.NumberValidationUtil;

@Component
@RequiredArgsConstructor
public class StudentCardNumberHandler implements RegistrationStateHandler {
    private final IsStudentExistsByStudentCardNumberInbound isStudentExistsByStudentCardNumberInbound;
    private final NumberValidationUtil  numberValidationUtil;

    @Override
    public StatusData handle(Student student, String input) {
        if (input == null || input.isBlank()) {
            return new StatusData(false, "Номер студ билета не может быть пустым!");
        }
        if (!isStudentCardNumber(input)) {
            return new StatusData(false, "Некорректный формат студ билета!");
        }
        if (isStudentExistsByStudentCardNumberInbound.execute(input)) {
            return new StatusData(false, "Студент с таким номером студенческого билета уже зарегистрирован");
        }
        student.setStudentCardNumber(input);
        return new StatusData(true, "");
    }

    @Override
    public RegistrationState currantState() {
        return RegistrationState.REGISTRATION_AWAITING_STUDENT_CARD;
    }

    @Override
    public RegistrationState nextState() {
        return RegistrationState.REGISTRATION_AWAITING_EMAIL;
    }

    public boolean isStudentCardNumber(String text) {
        return numberValidationUtil.isValidNumber(text);
    }
}
