package ru.samokhin.labCheck.adapter.bot.handler.rolebased.student.completeTask;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.ProcessHandler;
import ru.samokhin.labCheck.adapter.bot.keyboard.InlineKeyboardFactory;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskState;
import ru.samokhin.labCheck.adapter.bot.service.messaging.MessageSender;
import ru.samokhin.labCheck.adapter.bot.service.user.student.StudentService;
import ru.samokhin.labCheck.adapter.bot.service.user.student.completeTask.CompleteTaskService;
import ru.samokhin.labCheck.app.api.assignmentGroup.FindAssignmentGroupsByStudentTgChatIdInbound;
import ru.samokhin.labCheck.app.api.task.FindTaskByAssignmentGroupInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompleteTaskHandler implements ProcessHandler {
    private final MessageSender messageSender;
    private final CompleteTaskService completeTaskService;
    private final StudentService studentService;
    private final InlineKeyboardFactory inlineKeyboardFactory;
    private final FindAssignmentGroupsByStudentTgChatIdInbound findAssignmentGroupsByStudentTgChatIdInbound;
    private final FindTaskByAssignmentGroupInbound findTaskByAssignmentGroupInbound;

    private Message lastMessageForKbDeleting;

    @Override
    public void handleNonCommandUpdate(AbsSender absSender, Message message) {
        Long tgChatId = message.getFrom().getId();
        String messageText = message.getText();

        if (!completeTaskService.exists(tgChatId)) {
            completeTaskService.startCompletingTask(tgChatId);
            lastMessageForKbDeleting = askNextQuestion(absSender, tgChatId);
            return;
        }

        CompleteTaskState completeTaskState = completeTaskService.getState(tgChatId);
        switch (completeTaskState) {
            case COMPLETE_TASK_AWAITING_ASSIGNMENT_GROUP_NAME ,COMPLETE_TASK_AWAITING_TASK_NAME -> {
                messageSender.send(tgChatId, "Неверный способ взаимодействия с ботом", absSender);
                return;
            }
        }

        StatusData statusData = completeTaskService.updateState(tgChatId, messageText);
        if (!statusData.isSuccess()) {
            inlineKeyboardFactory.removeInlineKeyboard(absSender, lastMessageForKbDeleting);
            lastMessageForKbDeleting = messageSender.send(tgChatId, statusData.getMessage(), absSender,
                    inlineKeyboardFactory.createSingleButtonKeyboard("В меню", "В меню"));
            return;
        }
        InlineKeyboardMarkup inlineKeyboard = lastMessageForKbDeleting.getReplyMarkup();
        if (inlineKeyboard != null && inlineKeyboard.getKeyboard() != null && !inlineKeyboard.getKeyboard().isEmpty()) {
            inlineKeyboardFactory.removeInlineKeyboard(absSender, lastMessageForKbDeleting);
        }

        lastMessageForKbDeleting = askNextQuestion(absSender, tgChatId);

        if (completeTaskService.getState(tgChatId) == CompleteTaskState.COMPLETE_TASK_STATE_COMPLETE) {
            completeTaskService.removeCompletingTaskData(tgChatId);
            studentService.removeStudentData(tgChatId);
        }
    }

    @Override
    public void handleCallbackQuery(AbsSender absSender, CallbackQuery callbackQuery) {
        Long tgChatId = callbackQuery.getFrom().getId();
        String callbackData = callbackQuery.getData();

        switch (callbackData) {
            case "В меню" -> {
                completeTaskService.removeCompletingTaskData(tgChatId);
                studentService.removeStudentData(tgChatId);
                messageSender.send(tgChatId, "Вы вышли в главное меню!", absSender);
                inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
                return;
            }
        }

        StatusData statusData = completeTaskService.updateState(tgChatId, callbackData);
        if (!statusData.isSuccess()) {
            lastMessageForKbDeleting = messageSender.send(tgChatId, statusData.getMessage(), absSender,
                    inlineKeyboardFactory.createSingleButtonKeyboard("В меню", "В меню"));
            inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
            return;
        }
        lastMessageForKbDeleting = askNextQuestion(absSender, tgChatId);
        inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
    }

    private Message askNextQuestion(AbsSender absSender, Long tgChatId) {
        return messageSender.send(tgChatId, formTextForNextQuestion(tgChatId), absSender, formKeyboardForNextQuestion(tgChatId));
    }

    private String formTextForNextQuestion(Long tgChatId) {
        CompleteTaskState state = completeTaskService.getState(tgChatId);
        return switch (state) {
            case COMPLETE_TASK_AWAITING_ASSIGNMENT_GROUP_NAME -> "Выберите учебную группу:";
            case COMPLETE_TASK_AWAITING_TASK_NAME -> "Выберите задачу:";
            case COMPLETE_TASK_AWAITING_CODE_STR -> taskDescriptionText(tgChatId);
            case COMPLETE_TASK_STATE_COMPLETE -> answerDescriptionText(tgChatId);
        };
    }

    private String taskDescriptionText(Long tgChatId) {
        CompleteTaskContext context = completeTaskService.getContext(tgChatId);
        return String.format("""
                Код можно присылать неограниченное кол-во раз.
                В результат идет ваш последний запрос.
                
                Название задачи: %s
                
                Описание: %s
                
                Введите код:
                """, context.getTask().getName(), context.getTask().getDescription());
    }

    private String answerDescriptionText(Long tgChatId) {
        CompleteTaskContext context = completeTaskService.getContext(tgChatId);
        return String.format("""
                Ваш код принят!
                Ваш код прошел %d/%d тестов.
                """, context.getPassedTestCount(), context.getTotalTestCount());
    }

    private ReplyKeyboard formKeyboardForNextQuestion(Long tgChatId) {
        CompleteTaskState state = completeTaskService.getState(tgChatId);
        return switch (state) {
            case COMPLETE_TASK_AWAITING_ASSIGNMENT_GROUP_NAME -> inlineKeyboardFactory.createColumnKeyboard(getAssignedGroupDataMap(tgChatId));
            case COMPLETE_TASK_AWAITING_TASK_NAME -> inlineKeyboardFactory.createColumnKeyboard(getTaskDataMap(completeTaskService.getContext(tgChatId).getAssignmentGroup()));
            case COMPLETE_TASK_AWAITING_CODE_STR -> inlineKeyboardFactory.createSingleButtonKeyboard("В меню", "В меню");
            case COMPLETE_TASK_STATE_COMPLETE -> null;
        };
    }

    private Map<String, String> getAssignedGroupDataMap(Long tgChatId) {
        List<String> groupNames = findAssignmentGroupsByStudentTgChatIdInbound.execute(tgChatId);
        LinkedHashMap<String, String> result = groupNames.stream()
                .collect(Collectors.toMap(
                        name -> name,
                        name -> name,
                        (v1, v2) -> v2,
                        LinkedHashMap::new
                ));
        result.put("В меню", "В меню");
        return result;
    }

    private Map<String, String> getTaskDataMap(AssignmentGroup assignmentGroup) {
        List<Task> tasks = findTaskByAssignmentGroupInbound.execute(assignmentGroup);
        LinkedHashMap<String, String> result = tasks.stream()
                .collect(Collectors.toMap(
                        Task::getName,
                        Task::getName,
                        (v1, v2) -> v2,
                        LinkedHashMap::new
                ));
        result.put("В меню", "В меню");
        return result;
    }

}


