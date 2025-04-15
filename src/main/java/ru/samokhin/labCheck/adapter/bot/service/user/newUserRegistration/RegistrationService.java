package ru.samokhin.labCheck.adapter.bot.service.user.newUserRegistration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationContext;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.newUser.registration.state.RegistrationStateMachine;
import ru.samokhin.labCheck.domain.student.Student;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final Map<Long, RegistrationContext> userContexts = new ConcurrentHashMap<>();
    private final RegistrationStateMachine stateMachine;

    public boolean exists(Long tgChatId) {
        return userContexts.containsKey(tgChatId);
    }

    public void startRegistration(Long tgChatId) {
        Student student = new Student();
        student.setTgChatId(tgChatId);
        RegistrationContext context = new RegistrationContext(student, RegistrationState.REGISTRATION_AWAITING_FIRST_NAME);
        userContexts.put(tgChatId, context);
    }

    public Student getStudent(Long tgChatId) {
        return userContexts.get(tgChatId).getStudent();
    }

    public StatusData updateStatusData(Long tgChatId, String input) {
        RegistrationContext context = userContexts.get(tgChatId);
        if (context == null) {
            throw new RuntimeException("Регистрация не начата.");
        }
        return stateMachine.processInput(context, input);
    }

    public RegistrationState getState(Long tgChatId) {
        return userContexts.get(tgChatId).getCurrentState();
    }

    public RegistrationContext removeRegistrationData(Long tgChatId) {
        return userContexts.remove(tgChatId);
    }
}

