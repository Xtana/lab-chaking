package ru.samokhin.labCheck.adapter.bot.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.samokhin.labCheck.adapter.bot.handler.UserHandler;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;
import ru.samokhin.labCheck.adapter.bot.service.impl.NewUserRegistrationService;
import ru.samokhin.labCheck.app.api.student.StudentRepository;
import ru.samokhin.labCheck.app.api.studentGroup.StudentGroupRepository;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewUserHandler implements UserHandler {
    private final NewUserRegistrationService registrationService;
    private final StudentGroupRepository groupRepository;
    private final StudentRepository studentRepository;

    @Override
    public UserRole getRole() {
        return UserRole.NEW;
    }

    @Override
    public void handle(AbsSender absSender, Message message) {
        Long userId = message.getFrom().getId();
        String text = message.getText();
        System.out.println(text);

        if (!registrationService.userExists(userId)) {
            registrationService.startRegistration(userId, userId);
            sendMessage(absSender, userId, "Привет! Введи своё имя:");
            return;
        }
        registrationService.updateStudentData(userId, text);

        if (registrationService.isRegistrationCompleted(userId)) {
            Student student = registrationService.getCompletedStudent(userId);
            studentRepository.save(student);
            sendMessage(absSender, userId, "Регистрация завершена! Теперь ты студент.");
        } else {
            askNextQuestion(absSender, userId);
        }
    }



    private void askNextQuestion(AbsSender absSender, Long userId) {
        switch (registrationService.getState(userId)) {
            case AWAITING_LAST_NAME -> sendMessage(absSender, userId, "Введи свою фамилию:");
            case AWAITING_PATRONYMIC -> sendMessage(absSender, userId, "Введи отчество (или отправь '-' если нет):");
            case AWAITING_EMAIL -> sendMessage(absSender, userId, "Введи свою корпоративную почту:");
            case AWAITING_GROUP_SELECTION -> sendGroupSelection(absSender, userId);
            default -> {}
        }
    }


    private void sendGroupSelection(AbsSender absSender, Long userId) {
        List<StudentGroup> groups = groupRepository.findAll();
        StringBuilder message = new StringBuilder("Выбери свою группу (отправь номер):\n");
        for (int i = 0; i < groups.size(); i++) {
            message.append(i + 1).append(". ").append(groups.get(i).getName()).append("\n");
        }
        sendMessage(absSender, userId, message.toString());
    }

    private void sendMessage(AbsSender absSender, Long userId, String text) {
        SendMessage answer = new SendMessage();
        answer.setChatId(userId);

        answer.setText(text);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /start command", e);
        }
    }
}
