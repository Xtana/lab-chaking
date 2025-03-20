package ru.samokhin.labCheck.adapter.bot.handler.rolebased.newUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.UserHandler;
import ru.samokhin.labCheck.adapter.bot.keyboard.RegistrationKeyboardBuilder;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationStatusData;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;
import ru.samokhin.labCheck.adapter.bot.service.messaging.MessageSender;
import ru.samokhin.labCheck.adapter.bot.service.newUserRegistration.NewUserRegistrationService;
import ru.samokhin.labCheck.app.api.student.CreateStudentInbound;
import ru.samokhin.labCheck.domain.student.Student;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewUserHandler implements UserHandler {
    private final MessageSender messageSender;
    private final NewUserRegistrationService registrationService;
    private final CreateStudentInbound createStudentInbound;
    private final RegistrationKeyboardBuilder registrationKeyboardBuilder;

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
            messageSender.send(userId, "Привет новый пользователь!\nТебе необходимо заполнить информацию о себе:", absSender);
            askNextQuestion(absSender, userId);
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

        if (callbackData.startsWith("select_group_")) {
            handleGroupSelection(absSender, userId, callbackData);
            return;
        }

        switch (callbackData) {
            case "restart_registration" -> {
                registrationService.removeRegistrationData(userId);
                registrationService.startRegistration(userId, userId);
                messageSender.send(userId, "Начинаем регистрацию заново!", absSender);
                askNextQuestion(absSender, userId);
            }
            case "confirm_registration" -> {
                Student student = registrationService.removeRegistrationData(userId).getStudent();
                createStudentInbound.execute(
                        student.getFirstName(),
                        student.getPatronymic(),
                        student.getLastName(),
                        student.getStudentGroup().getName(),
                        student.getEmail(),
                        student.getStudentCardNumber(),
                        student.getTgChatId()); //TODO добавить проверку успешности операции
                messageSender.send(userId, "Регистрация завершена успешно!", absSender);
            }
            default -> throw new RuntimeException("Неизвестная команда");
        }
    }

    private void askNextQuestion(AbsSender absSender, Long userId) {
        messageSender.send(userId, formTextForNextQuestion(userId), absSender, formKeyboardForNextQuestion(userId));
    }

    private String formTextForNextQuestion(Long userId) {
        RegistrationState state = registrationService.getState(userId);
        return switch (state) {
            case AWAITING_FIRST_NAME -> "Введи свое имя:";
            case AWAITING_LAST_NAME -> "Введи свою фамилию:";
            case AWAITING_PATRONYMIC -> "Введи свое отчество (или '-')";
            case AWAITING_STUDENT_CARD_NUMBER -> "Введи номер студенческого билета:";
            case AWAITING_EMAIL -> "Введи корпоративную почту:";
            case AWAITING_GROUP_SELECTION -> "Выбери свою группу:";
            case COMPLETED -> buildStudentInfo(userId);
        };
    }

    private String buildStudentInfo(Long userId) {
        Student student = registrationService.getStudent(userId).getStudent();
        return String.format("Имя: %s\nФамилия: %s\nОтчество: %s\nEmail: %s\nНомер зачетки: %s\nГруппа: %s",
                student.getFirstName(),
                student.getLastName(),
                student.getPatronymic() == null ? "-" : student.getPatronymic(),
                student.getEmail(),
                student.getStudentCardNumber(),
                student.getStudentGroup().getName());
    }

    private InlineKeyboardMarkup formKeyboardForNextQuestion(Long userId) {
        RegistrationState state = registrationService.getState(userId);
        return switch (state) {
            case AWAITING_FIRST_NAME -> null;
            case AWAITING_LAST_NAME, AWAITING_PATRONYMIC,
                 AWAITING_STUDENT_CARD_NUMBER, AWAITING_EMAIL -> registrationKeyboardBuilder.getRestartButtonKeyboard();
            case AWAITING_GROUP_SELECTION -> registrationKeyboardBuilder.getGroupSelection();
            case COMPLETED -> registrationKeyboardBuilder.getConfirmationKeyboard();
        };
    }

    private void handleGroupSelection(AbsSender absSender, Long userId, String callbackData) {
        String groupName = callbackData.replace("select_group_", "");
        registrationService.updateStudentData(userId, groupName);
        askNextQuestion(absSender, userId);
    }
}
