package ru.samokhin.labCheck.adapter.bot.handler.rolebased.newUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.UserHandler;
import ru.samokhin.labCheck.adapter.bot.model.RegistrationStatusData;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;
import ru.samokhin.labCheck.adapter.bot.service.messaging.MessageSender;
import ru.samokhin.labCheck.adapter.bot.service.newUserRegistration.NewUserRegistrationService;
import ru.samokhin.labCheck.app.api.student.StudentRepository;
import ru.samokhin.labCheck.app.api.studentGroup.StudentGroupRepository;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewUserHandler implements UserHandler {
    private final MessageSender messageSender;
    private final NewUserRegistrationService registrationService;
    private final StudentGroupRepository groupRepository;
    private final StudentRepository studentRepository;

    @Override
    public UserRole getRole() {
        return UserRole.NEW;
    }

    @Override
    public void handleNonCommandUpdate(AbsSender absSender, Message message) {
        Long userId = message.getFrom().getId();
        String text = message.getText();
        System.out.println(text);

        if (!registrationService.userExists(userId)) {
            registrationService.startRegistration(userId, userId);
            messageSender.send(userId, "Привет новый ользователь!\nТебе необходимо заполнить информацию о себе.", absSender);
            messageSender.send(userId, "Введи своё имя:", absSender);
            return;
        }

        RegistrationStatusData registrationStatusData = registrationService.updateStudentData(userId, text);
        if (!registrationStatusData.isSuccess()) {
            messageSender.send(userId, registrationStatusData.getMessage(), absSender);
        }
        askNextQuestion(absSender, userId);

    }

    @Override
    public void handleCallbackQuery(AbsSender absSender, CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();
        String callbackData = callbackQuery.getData();
        System.out.println(callbackData);

        if (callbackData.startsWith("select_group_")) {
            handleGroupSelection(absSender, userId, callbackData);
            return;
        }

        switch (callbackData) {
            case "restart_registration" -> {
                registrationService.delRegistrationData(userId);
                registrationService.startRegistration(userId, userId);
                messageSender.send(userId, "Начинаем регистрацию заново. Введи своё имя:", absSender);
            }
            case "confirm_registration" -> {
                Student student = registrationService.removeStudent(userId);
                studentRepository.save(student);
                messageSender.send(userId, "Регистрация завершена!", absSender);
            }
            default -> messageSender.send(userId, "❌ Неизвестная команда.", absSender);
        }
    }

    private void handleGroupSelection(AbsSender absSender, Long userId, String callbackData) {
        String groupName = callbackData.replace("select_group_", "");
        registrationService.updateStudentData(userId, groupName);
        askNextQuestion(absSender, userId);
    }



    private void askNextQuestion(AbsSender absSender, Long userId) {
        switch (registrationService.getState(userId)) {
            case AWAITING_LAST_NAME -> {
                messageSender.send(userId, "Введи свою фамилию:", absSender, getRestartButtonKeyboard());
            }
            case AWAITING_PATRONYMIC -> {
                messageSender.send(userId, "Введи свое отчество (или отправь '-' если нет):", absSender, getRestartButtonKeyboard());
            }
            case AWAITING_STUDENT_CARD_NUMBER -> {
                messageSender.send(userId, "Введи номер студенческого билета (только цифры):", absSender, getRestartButtonKeyboard());
            }
            case AWAITING_EMAIL -> {
                messageSender.send(userId, "Введи корпаративную почту:", absSender, getRestartButtonKeyboard());
            }
            case AWAITING_GROUP_SELECTION -> sendGroupSelection(absSender, userId);
            case COMPLETED -> {
                Student student = registrationService.getStudent(userId);
                String studentInfo = String.format("Имя: %s\nФамилия: %s\nОтчество: %s\nEmail: %s\nНомер зачетки: %s\nГруппа: %s",
                        student.getFirstName(),
                        student.getLastName(),
                        student.getPatronymic() == null ? "-" : student.getPatronymic(),
                        student.getEmail(),
                        student.getStudentCardNumber(),
                        student.getGroupName().getName());
                messageSender.send(userId, studentInfo, absSender, getConfirmationKeyboard());}
            default -> {}
        }
    }

    private void sendGroupSelection(AbsSender absSender, Long userId) {
        List<StudentGroup> groups = groupRepository.findAll();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (StudentGroup group : groups) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(group.getName());
            button.setCallbackData("select_group_" + group.getName());
            keyboard.add(Collections.singletonList(button));
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        messageSender.send(userId, "Выбери свою группу:", absSender, markup);
    }

    private InlineKeyboardMarkup getRestartButtonKeyboard() {
        InlineKeyboardButton restartButton = new InlineKeyboardButton();
        restartButton.setText("Начать заново");
        restartButton.setCallbackData("restart_registration");
        List<InlineKeyboardButton> row = Collections.singletonList(restartButton);
        List<List<InlineKeyboardButton>> keyboard = Collections.singletonList(row);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    private InlineKeyboardMarkup getConfirmationKeyboard() {
        InlineKeyboardButton confirmButton = new InlineKeyboardButton();
        confirmButton.setText("Подтвердить данные");
        confirmButton.setCallbackData("confirm_registration");
        InlineKeyboardButton restartButton = new InlineKeyboardButton();
        restartButton.setText("Начать занова");
        restartButton.setCallbackData("restart_registration");
        List<InlineKeyboardButton> row = Arrays.asList(confirmButton, restartButton);
        List<List<InlineKeyboardButton>> keyboard = Collections.singletonList(row);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }
}
