package ru.samokhin.labCheck.adapter.bot.service.newUserRegistration.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationStatusData;
import ru.samokhin.labCheck.app.api.student.IsStudentExistsByStudentCardNumberInbound;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.staging.validationUtils.NumberValidationUtil;

@Component
@RequiredArgsConstructor
public class StudentCardNumberHandler implements RegistrationStateHandler {
    private final IsStudentExistsByStudentCardNumberInbound isStudentExistsByStudentCardNumberInbound;
    private final NumberValidationUtil  numberValidationUtil;

    @Override
    public RegistrationStatusData handle(Student student, String input) {
        if (input == null || input.isBlank()) {
            return new RegistrationStatusData(false, "Номер студ билета не может быть пустым!");
        }
        if (!isStudentCardNumber(input)) {
            return new RegistrationStatusData(false, "Некорректный формат студ билета!");
        }
        if (isStudentExistsByStudentCardNumberInbound.execute(input)) {
            return new RegistrationStatusData(false, "Студент с таким номером студенческого билета уже зарегистрирован");
        }
        student.setStudentCardNumber(input);
        return new RegistrationStatusData(true, "");
    }

    @Override
    public RegistrationState currantState() {
        return RegistrationState.AWAITING_STUDENT_CARD_NUMBER;
    }

    @Override
    public RegistrationState nextState() {
        return RegistrationState.AWAITING_EMAIL;
    }

    public boolean isStudentCardNumber(String text) {
        return numberValidationUtil.isValidNumber(text);
    }
}
