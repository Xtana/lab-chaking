package ru.samokhin.labCheck.adapter.bot.service.newUserRegistration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationContext;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationStatusData;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.service.newUserRegistration.state.RegistrationStateMachine;
import ru.samokhin.labCheck.domain.student.Student;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NewUserRegistrationService {
    private final Map<Long, RegistrationContext> userContexts = new ConcurrentHashMap<>();
    private final RegistrationStateMachine stateMachine;

    public boolean userExists(Long userId) {
        return userContexts.containsKey(userId);
    }

    public void startRegistration(Long userId, Long tgChatId) {
        Student student = new Student();
        student.setTgChatId(tgChatId);
        RegistrationContext context = new RegistrationContext(student, RegistrationState.AWAITING_FIRST_NAME);
        userContexts.put(userId, context);
    }

    public RegistrationStatusData updateStudentData(Long userId, String input) {
        RegistrationContext context = userContexts.get(userId);
        if (context == null) {
            throw new RuntimeException("Регистрация не начата.");
        }
        return stateMachine.processInput(context, input);
    }

    public RegistrationContext removeRegistrationData(Long userId) {
        return userContexts.remove(userId);
    }

    public RegistrationContext getStudent(Long userId) {
        return userContexts.get(userId);
    }

    public RegistrationState getState(Long userId) {
        return userContexts.get(userId).getCurrentState();
    }
}

