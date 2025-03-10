package ru.samokhin.labCheck.adapter.bot.service.newUserRegistration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.samokhin.labCheck.adapter.bot.model.RegistrationStatusData;
import ru.samokhin.labCheck.adapter.bot.model.RegistrationState;
import ru.samokhin.labCheck.app.api.student.IsStudentExistsByEmailInbound;
import ru.samokhin.labCheck.app.api.student.IsStudentExistsByStudentCardNumberInbound;
import ru.samokhin.labCheck.app.api.studentGroup.FindStudentGroupByNameInbound;
import ru.samokhin.labCheck.domain.student.Student;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static ru.samokhin.labCheck.adapter.bot.model.RegistrationState.*;

@Service
@RequiredArgsConstructor
public class NewUserRegistrationService {
    private final IsStudentExistsByEmailInbound isStudentExistsByEmailInbound;
    private final IsStudentExistsByStudentCardNumberInbound isStudentExistsByStudentCardNumberInbound;
    private final FindStudentGroupByNameInbound findStudentGroupByNameInbound;
    private final Map<Long, Student> userData = new HashMap<>();
    private final Map<Long, RegistrationState> userStates = new HashMap<>();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@([A-Za-z0-9.-]+)\\.([A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE);
    private static final Pattern STUDENT_CARD_NUMBER_PATTERN = Pattern.compile("^\\d+$");

    public boolean userExists(Long userId) {
        return userStates.containsKey(userId);
    }

    public void delRegistrationData(Long userId) {
        userData.remove(userId);
        userStates.remove(userId);
    }

    public void startRegistration(Long userId, Long tgChatId) {
        Student student = new Student();
        student.setTgChatId(tgChatId);
        userData.put(userId, student);
        userStates.put(userId, AWAITING_FIRST_NAME);
    }

    public RegistrationStatusData updateStudentData(Long userId, String data) {
        Student student = userData.get(userId);
        RegistrationState state = userStates.get(userId);

        switch (state) {
            case AWAITING_FIRST_NAME -> {
                student.setFirstName(data);
                userStates.put(userId, AWAITING_LAST_NAME);
                return new RegistrationStatusData(true, "");
            }
            case AWAITING_LAST_NAME -> {
                student.setLastName(data);
                userStates.put(userId, AWAITING_PATRONYMIC);
                return new RegistrationStatusData(true, "");
            }
            case AWAITING_PATRONYMIC -> {
                student.setPatronymic(data.equals("-") ? null : data);
                userStates.put(userId, AWAITING_STUDENT_CARD_NUMBER);
                return new RegistrationStatusData(true, "");
            }
            case AWAITING_STUDENT_CARD_NUMBER -> {
                if (!isStudentCardNumber(data)) {
                    return new RegistrationStatusData(false, "Некорректный формат студ билета!");
                }

                if (isStudentExistsByStudentCardNumberInbound.execute(data)) {
                    return new RegistrationStatusData(false, "Студент с таким номером студенческого билета уже зарегестрирован");
                }
                student.setStudentCardNumber(data);
                userStates.put(userId, AWAITING_EMAIL);
                return new RegistrationStatusData(true, "");
            }
            case AWAITING_EMAIL -> {
                if (!isValidEmail(data)) {
                    return new RegistrationStatusData(false, "Некорректный формат email!");
                }

                if (isStudentExistsByEmailInbound.execute(data)) {
                    return new RegistrationStatusData(false, "Студент с таким email уже зарегестрирован");
                }
                student.setEmail(data);
                userStates.put(userId, AWAITING_GROUP_SELECTION);
                return new RegistrationStatusData(true, "");
            }
            case AWAITING_GROUP_SELECTION -> {
                student = userData.get(userId);
                student.setGroupName(findStudentGroupByNameInbound.execute(data));
                userStates.put(userId, COMPLETED);
                return new RegistrationStatusData(true, "");
            }
        }
        return null;
    }

    public boolean isRegistrationCompleted(Long userId) {
        return userStates.get(userId) == COMPLETED;
    }

    public Student removeStudent(Long userId) {
        return userData.remove(userId);
    }

    public Student getStudent(Long userId) {
        return userData.get(userId);
    }

    public RegistrationState getState(Long userId) {
        return userStates.get(userId);
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isStudentCardNumber(String text) {
        return STUDENT_CARD_NUMBER_PATTERN.matcher(text).matches();
    }
}

