package ru.samokhin.labCheck.adapter.bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.samokhin.labCheck.adapter.bot.model.RegistrationState;
import ru.samokhin.labCheck.app.api.studentGroup.FindStudentGroupByNameInbound;
import ru.samokhin.labCheck.domain.student.Student;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NewUserRegistrationService {
    private final FindStudentGroupByNameInbound findStudentGroupByNameInbound;
    private final Map<Long, Student> userData = new HashMap<>();
    private final Map<Long, RegistrationState> userStates = new HashMap<>();

    public boolean userExists(Long userId) {
        return userStates.containsKey(userId);
    }

    public void startRegistration(Long userId, Long tgChatId) {
        Student student = new Student();
        student.setTgChatId(tgChatId);
        userData.put(userId, student);
        userStates.put(userId, RegistrationState.AWAITING_FIRST_NAME);
    }

    public void updateStudentData(Long userId, String data) {
        Student student = userData.get(userId);
        RegistrationState state = userStates.get(userId);

        switch (state) {
            case AWAITING_FIRST_NAME -> {
                student.setFirstName(data);
                userStates.put(userId, RegistrationState.AWAITING_LAST_NAME);
            }
            case AWAITING_LAST_NAME -> {
                student.setLastName(data);
                userStates.put(userId, RegistrationState.AWAITING_PATRONYMIC);
            }
            case AWAITING_PATRONYMIC -> {
                student.setPatronymic(data.isEmpty() ? null : data);
                userStates.put(userId, RegistrationState.AWAITING_EMAIL);
            }
            case AWAITING_EMAIL -> {
                student.setEmail(data);
                student.setStudentCardNumber(parseStudentCardFromEmail(data));
                userStates.put(userId, RegistrationState.AWAITING_GROUP_SELECTION);
            }
            case AWAITING_GROUP_SELECTION -> {
                student = userData.get(userId);
                student.setGroupName(findStudentGroupByNameInbound.execute(data));
                userStates.put(userId, RegistrationState.COMPLETED);
            }
        }
    }

    public boolean isRegistrationCompleted(Long userId) {
        return userStates.get(userId) == RegistrationState.COMPLETED;
    }

    public Student getCompletedStudent(Long userId) {
        return userData.remove(userId);
    }

    public RegistrationState getState(Long userId) {
        return userStates.get(userId);
    }

    private String parseStudentCardFromEmail(String email) {
        return email.replaceAll("\\D", ""); // Оставляет только цифры
    }

}

