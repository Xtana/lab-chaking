package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createTask;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.ProcessHandler;
import ru.samokhin.labCheck.adapter.bot.keyboard.teacher.CreateTaskInlineKeyboardBuilder;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskState;
import ru.samokhin.labCheck.adapter.bot.service.messaging.MessageSender;
import ru.samokhin.labCheck.adapter.bot.service.user.teacher.createTask.CreateTaskService;
import ru.samokhin.labCheck.app.api.assignmentGroup.GetAllAssignmentGroupsNameStringInbound;
import ru.samokhin.labCheck.app.api.studentGroup.GetAllStudentGroupNameStringsInbound;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateTaskHandler implements ProcessHandler {
    private final MessageSender messageSender;
    private final CreateTaskService createTaskService;
    private final CreateTaskInlineKeyboardBuilder createTaskKeyboardBuilder;
    private final GetAllAssignmentGroupsNameStringInbound getAllAssignmentGroupsNameStringInbound;
    private final GetAllStudentGroupNameStringsInbound getAllStudentGroupNameStringsInbound;
    private Message lastMessageForKbDeleting;

    private static final Map<String, String> COMPLETE_STATE_BUTTON_DATA = new HashMap<>() {{
        put("Подтвердить данные", "Подтвердить данные");
        put("Начать заново", "Начать заново");
        put("В меню", "В меню");
    }};

    private static final Map<String, String> CHANGE_STSTE_BUTTON_DATA = new HashMap<>() {{
        put("Начать заново", "Начать заново");
        put("В меню", "В меню");
    }};

    private static final Map<String, String> YES_NO_BUTTON_DATA = new HashMap<>() {{
        put("Да", "Да");
        put("Нет", "Нет");
    }};

    @Override
    public void handleNonCommandUpdate(AbsSender absSender, Message message) {
        Long tgChatId = message.getFrom().getId();
        String messageText = message.getText();

        if (!createTaskService.exists(tgChatId)) {
            StatusData statusData = createTaskService.startCreatingTask(tgChatId);
            if (!statusData.isSuccess()) {
                messageSender.send(tgChatId, statusData.getMessage(), absSender);
                return;
            }
            lastMessageForKbDeleting = askNextQuestion(absSender, tgChatId);
            return;
        }

        CreateTaskState createTaskState = createTaskService.getState(tgChatId);
        switch (createTaskState) {
            case CREATE_TASK_AWAITING_ASSIGNMENT_GROUP_NAME, CREATE_TASK_AWAITING_STUDENT_GROUP,
                 CREATE_TASK_AWAITING_MORE_STUDENT_GROUP, CREATE_TASK_AWAITING_MORE_TEST,
                 CREATE_TASK_FILL_DATA -> {
                messageSender.send(tgChatId, "Неверный способ взаимодействия с ботом", absSender);
                return;
            }
        }

        StatusData statusData = createTaskService.updateStatusData(tgChatId, messageText);
        if (!statusData.isSuccess()) {
            messageSender.send(tgChatId, statusData.getMessage(), absSender);
            return;
        }
        InlineKeyboardMarkup inlineKeyboard = lastMessageForKbDeleting.getReplyMarkup();
        if (inlineKeyboard != null && inlineKeyboard.getKeyboard() != null && !inlineKeyboard.getKeyboard().isEmpty()) {
            createTaskKeyboardBuilder.removeInlineKeyboard(absSender, lastMessageForKbDeleting);
        }

        lastMessageForKbDeleting = askNextQuestion(absSender, tgChatId);
    }

    @Override
    public void handleCallbackQuery(AbsSender absSender, CallbackQuery callbackQuery) {
        Long tgChatId = callbackQuery.getFrom().getId();
        String callbackData = callbackQuery.getData();

        switch (callbackData) {
            case "Начать заново" -> {
                createTaskService.removeCreateTaskData(tgChatId);
                createTaskService.startCreatingTask(tgChatId);
                messageSender.send(tgChatId, "Начинаем создание задачи заново!", absSender);
                lastMessageForKbDeleting = askNextQuestion(absSender, tgChatId);
                createTaskKeyboardBuilder.removeInlineKeyboard(absSender, callbackQuery);
                return;
            }
            case "В меню" -> {

                return;
            }
        }

        StatusData statusData = createTaskService.updateStatusData(tgChatId, callbackData);
        if (!statusData.isSuccess()) {
            lastMessageForKbDeleting = messageSender.send(tgChatId, statusData.getMessage(), absSender,
                    createTaskKeyboardBuilder.getRowInlineButton(CHANGE_STSTE_BUTTON_DATA));
            createTaskKeyboardBuilder.removeInlineKeyboard(absSender, callbackQuery);
            return;
        }
        lastMessageForKbDeleting = askNextQuestion(absSender, tgChatId);
        createTaskKeyboardBuilder.removeInlineKeyboard(absSender, callbackQuery);
    }

    private Message askNextQuestion(AbsSender absSender, Long tgChatId) {
        return messageSender.send(tgChatId, formTextForNextQuestion(tgChatId), absSender, formKeyboardForNextQuestion(tgChatId));
    }

    private String formTextForNextQuestion(Long tgChatId) {
        CreateTaskState state = createTaskService.getState(tgChatId);
        return switch (state) {
            case CREATE_TASK_AWAITING_ASSIGNMENT_GROUP_NAME -> "Выбери группу занятий:";
            case CREATE_TASK_AWAITING_TASK_NAME -> "Введи название задания:";
            case CREATE_TASK_AWAITING_TASK_DESCRIPTION -> "Введи описание к заданию:";
            case CREATE_TASK_AWAITING_STUDENT_GROUP -> "Выберете учебную группу:";
            case CREATE_TASK_AWAITING_MORE_STUDENT_GROUP -> "Добавить еще учебную группу?";
            case CREATE_TASK_AWAITING_TEST_NAME -> "Введите название теста";
            case CREATE_TASK_AWAITING_TEST -> "Введите текст теста:";
            case CREATE_TASK_AWAITING_MORE_TEST -> "Добавить еще тест?";
            case CREATE_TASK_FILL_DATA -> buildTaskInfo(tgChatId);
            case CREATE_TASK_COMPLETED -> "Задача успешно создана!";
        };
    }

    private String buildTaskInfo(Long tgChatId) {
        Task task = createTaskService.getContext(tgChatId).getTask();
        return String.format("Название: %s\nОписание: %s\nНазвание группы: %s\nСписок учебных групп: %s",
                task.getName(),
                task.getDescription(),
                task.getAssignmentGroup().getName(),
                task.getStudentGroups().stream()
                        .map(StudentGroup::getName)
                        .collect(Collectors.joining(", "))
        );
    }

    private InlineKeyboardMarkup formKeyboardForNextQuestion(Long tgChatId) {
        CreateTaskState state = createTaskService.getState(tgChatId);
        return switch (state) {
            case CREATE_TASK_AWAITING_ASSIGNMENT_GROUP_NAME -> createTaskKeyboardBuilder.getColumnInlineButton(getAssignedGroupDataMap());
            case CREATE_TASK_AWAITING_TASK_NAME, CREATE_TASK_AWAITING_TASK_DESCRIPTION,
                 CREATE_TASK_AWAITING_TEST, CREATE_TASK_AWAITING_TEST_NAME
                    -> createTaskKeyboardBuilder.getRowInlineButton(CHANGE_STSTE_BUTTON_DATA);
            case CREATE_TASK_AWAITING_STUDENT_GROUP -> createTaskKeyboardBuilder.getColumnInlineButton(getStudentGroupDataMap(tgChatId));
            case CREATE_TASK_AWAITING_MORE_TEST, CREATE_TASK_AWAITING_MORE_STUDENT_GROUP -> createTaskKeyboardBuilder.getRowInlineButton(YES_NO_BUTTON_DATA);
            case CREATE_TASK_FILL_DATA -> createTaskKeyboardBuilder.getRowInlineButton(COMPLETE_STATE_BUTTON_DATA);
            case CREATE_TASK_COMPLETED -> null;
        };
    }

    private Map<String, String> getAssignedGroupDataMap() {
        List<String> groupNames = getAllAssignmentGroupsNameStringInbound.execute();
        return groupNames.stream()
                .collect(Collectors.toMap(
                        name -> name,
                        name -> name
                ));
    }

    private Map<String, String> getStudentGroupDataMap(Long tgChatId) {
        // Получаем все группы
        List<String> allGroupNames = getAllStudentGroupNameStringsInbound.execute();

        // Безопасно извлекаем уже выбранные группы
        List<String> selectedGroupNames = createTaskService.getContext(tgChatId) != null &&
                createTaskService.getContext(tgChatId).getTask() != null &&
                createTaskService.getContext(tgChatId).getTask().getStudentGroups() != null
                ? createTaskService.getContext(tgChatId).getTask().getStudentGroups().stream()
                .map(StudentGroup::getName)
                .collect(Collectors.toList())
                : List.of(); // если null — значит ничего не выбрано

        // Фильтруем уже выбранные
        return allGroupNames.stream()
                .filter(name -> !selectedGroupNames.contains(name))
                .collect(Collectors.toMap(
                        name -> name,
                        name -> name
                ));
    }
}
