package ru.samokhin.labCheck.adapter.bot.service.newUserRegistration.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationStatusData;
import ru.samokhin.labCheck.app.api.student.IsStudentExistsByEmailInbound;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.staging.validationUtils.EmailValidationUtil;

@Component
@RequiredArgsConstructor
public class EmailHandler implements RegistrationStateHandler {
    private final IsStudentExistsByEmailInbound isStudentExistsByEmailInbound;
    private final EmailValidationUtil emailValidationUtil;

    @Override
    public RegistrationStatusData handle(Student student, String input) {
        if (input == null || input.isBlank()) {
            return new RegistrationStatusData(false, "Email не может быть пустым!");
        }
        if (!isValidEmail(input)) {
            return new RegistrationStatusData(false, "Некорректный формат email!");
        }
        if (isStudentExistsByEmailInbound.execute(input)) {
            return new RegistrationStatusData(false, "Студент с таким email уже зарегистрирован");
        }
        student.setEmail(input);
        return new RegistrationStatusData(true, "");
    }

    @Override
    public RegistrationState currantState() {
        return RegistrationState.AWAITING_EMAIL;
    }


    @Override
    public RegistrationState nextState() {
        return RegistrationState.AWAITING_GROUP_SELECTION;
    }

    private boolean isValidEmail(String email) {
        return emailValidationUtil.isValidEmail(email);
    }


}