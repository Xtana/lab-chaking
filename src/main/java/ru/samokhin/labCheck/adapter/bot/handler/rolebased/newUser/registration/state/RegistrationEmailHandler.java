package ru.samokhin.labCheck.adapter.bot.handler.rolebased.newUser.registration.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.app.api.student.IsStudentExistsByEmailInbound;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.staging.validationUtils.EmailValidationUtil;

@Component
@RequiredArgsConstructor
public class RegistrationEmailHandler implements RegistrationStateHandler {
    private final IsStudentExistsByEmailInbound isStudentExistsByEmailInbound;
    private final EmailValidationUtil emailValidationUtil;

    @Override
    public StatusData handle(Student student, String input) {
        if (input == null || input.isBlank()) {
            return new StatusData(false, "Email не может быть пустым!");
        }
        if (!isValidEmail(input)) {
            return new StatusData(false, "Некорректный формат email!");
        }
        if (isStudentExistsByEmailInbound.execute(input)) {
            return new StatusData(false, "Студент с таким email уже зарегистрирован");
        }
        student.setEmail(input);
        return new StatusData(true, "");
    }

    @Override
    public RegistrationState currantState() {
        return RegistrationState.REGISTRATION_AWAITING_EMAIL;
    }


    @Override
    public RegistrationState nextState() {
        return RegistrationState.REGISTRATION_AWAITING_STUDENT_GROUP;
    }

    private boolean isValidEmail(String email) {
        return emailValidationUtil.isValidEmail(email);
    }


}