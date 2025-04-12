package ru.samokhin.labCheck.adapter.bot.handler.rolebased.newUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.UserHandler;
import ru.samokhin.labCheck.adapter.bot.keyboard.newUser.RegistrationInlineKeyboardBuilder;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;
import ru.samokhin.labCheck.adapter.bot.service.messaging.MessageSender;
import ru.samokhin.labCheck.adapter.bot.service.user.newUserRegistration.RegistrationService;
import ru.samokhin.labCheck.app.api.student.CreateStudentInbound;
import ru.samokhin.labCheck.app.api.studentGroup.GetAllStudentGroupNameStringsInbound;
import ru.samokhin.labCheck.domain.student.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewUserHandler implements UserHandler {
    private final MessageSender messageSender;
    private final RegistrationService registrationService;
    private final CreateStudentInbound createStudentInbound;
    private final RegistrationInlineKeyboardBuilder registrationKeyboardBuilder;
    private final GetAllStudentGroupNameStringsInbound getAllStudentGroupNameStringsInbound;

    private static final Map<String, String> COMPLITE_STATE_BUTTON_DATA = new HashMap<>() {{
        put("confirm_registration", "Подтвердить данные");
        put("restart_registration", "Начать заново");
    }};

    @Override
    public UserRole getRole() {
        return UserRole.NEW;
    }

    @Override
    public void handleNonCommandUpdate(AbsSender absSender, Message message) {
        Long tgChatId = message.getFrom().getId();
        String messageText = message.getText();
        System.out.println(tgChatId + " - " + messageText);

        if (!registrationService.exists(tgChatId)) {
            registrationService.startRegistration(tgChatId);
            messageSender.send(tgChatId, "Привет новый пользователь!\nТебе необходимо заполнить информацию о себе:", absSender);
            askNextQuestion(absSender, tgChatId);
            return;
        }

        // Если вдруг использовали ввод с клавиатуры для случаев, где нужно нажать кнопку
        RegistrationState registrationState = registrationService.getState(tgChatId);
        if (registrationState == RegistrationState.REGISTRATION_AWAITING_STUDENT_GROUP ||
                registrationState == RegistrationState.REGISTRATION_COMPLETED) {
            messageSender.send(tgChatId, "Неверный способ взаимодействия с ботом, используйте кнопки!", absSender);
            return;
        }

        StatusData statusData = registrationService.updateStatusData(tgChatId, messageText);
        if (!statusData.isSuccess()) {
            messageSender.send(tgChatId, statusData.getMessage(), absSender);
        }
        askNextQuestion(absSender, tgChatId);
    }

    @Override
    public void handleCallbackQuery(AbsSender absSender, CallbackQuery callbackQuery) {
        Long tgChatId = callbackQuery.getFrom().getId();
        String callbackData = callbackQuery.getData();

        if (callbackData.startsWith("select_group_")) {
            handleGroupSelection(absSender, tgChatId, callbackData);
            return;
        }

        switch (callbackData) {
            case "restart_registration" -> {
                registrationService.removeRegistrationData(tgChatId);
                registrationService.startRegistration(tgChatId);
                messageSender.send(tgChatId, "Начинаем регистрацию заново!", absSender);
                askNextQuestion(absSender, tgChatId);
            }
            case "confirm_registration" -> {
                String massage = """
                            Регистрация завершена успешно!
                            Еще раз нажмите на команду /start и приступайте к выполнению задания""";
                Student student = registrationService.removeRegistrationData(tgChatId).getStudent();
                try {
                    createStudentInbound.execute(
                            student.getFirstName(),
                            student.getPatronymic(),
                            student.getLastName(),
                            student.getStudentGroup().getName(),
                            student.getEmail(),
                            student.getStudentCardNumber(),
                            student.getTgChatId());
                } catch (Exception e) {
                    massage = "Произошла ошибка при регистрации.";
                }
                messageSender.send(tgChatId, massage, absSender);
            }
            default -> throw new RuntimeException("Неизвестная команда");
        }
    }

    private void askNextQuestion(AbsSender absSender, Long tgChatId) {
        messageSender.send(tgChatId, formTextForNextQuestion(tgChatId), absSender, formKeyboardForNextQuestion(tgChatId));
    }

    private String formTextForNextQuestion(Long tgChatId) {
        RegistrationState state = registrationService.getState(tgChatId);
        return switch (state) {
            case REGISTRATION_AWAITING_FIRST_NAME -> "Введи свое имя:";
            case REGISTRATION_AWAITING_LAST_NAME -> "Введи свою фамилию:";
            case REGISTRATION_AWAITING_PATRONYMIC -> "Введи свое отчество (или '-')";
            case REGISTRATION_AWAITING_STUDENT_CARD -> "Введи номер студенческого билета:";
            case REGISTRATION_AWAITING_EMAIL -> "Введи корпоративную почту:";
            case REGISTRATION_AWAITING_STUDENT_GROUP -> "Выбери свою группу:";
            case REGISTRATION_COMPLETED -> buildStudentInfo(tgChatId);
        };
    }

    private String buildStudentInfo(Long tgChatId) {
        Student student = registrationService.getStudent(tgChatId);
        return String.format("Имя: %s\nФамилия: %s\nОтчество: %s\nEmail: %s\nНомер зачетки: %s\nГруппа: %s",
                student.getFirstName(),
                student.getLastName(),
                student.getPatronymic() == null ? "-" : student.getPatronymic(),
                student.getEmail(),
                student.getStudentCardNumber(),
                student.getStudentGroup().getName());
    }

    private InlineKeyboardMarkup formKeyboardForNextQuestion(Long tgChatId) {
        RegistrationState state = registrationService.getState(tgChatId);
        return switch (state) {
            case REGISTRATION_AWAITING_FIRST_NAME -> null;
            case REGISTRATION_AWAITING_LAST_NAME, REGISTRATION_AWAITING_PATRONYMIC,
                 REGISTRATION_AWAITING_STUDENT_CARD, REGISTRATION_AWAITING_EMAIL -> registrationKeyboardBuilder.getSingleInlineButton("Начать заново", "restart_registration");
            case REGISTRATION_AWAITING_STUDENT_GROUP -> registrationKeyboardBuilder.getColumnInlineButton(getGroupDataMap());
            case REGISTRATION_COMPLETED -> registrationKeyboardBuilder.getRowInlineButton(COMPLITE_STATE_BUTTON_DATA);
        };
    }

    private void handleGroupSelection(AbsSender absSender, Long tgChatId, String callbackData) {
        String groupName = callbackData.replace("select_group_", "");
        registrationService.updateStatusData(tgChatId, groupName);
        askNextQuestion(absSender, tgChatId);
    }

    public Map<String, String> getGroupDataMap() {
        List<String> groupNames = getAllStudentGroupNameStringsInbound.execute();
        return groupNames.stream()
                .collect(Collectors.toMap(
                        name -> "select_group_" + name, // key: callbackData
                        name -> name                    // value: button text
                ));
    }

}
