package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.deleteTask;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.ProcessHandler;
import ru.samokhin.labCheck.adapter.bot.keyboard.InlineKeyboardFactory;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.deleteTask.DeleteTaskState;
import ru.samokhin.labCheck.adapter.bot.service.messaging.MessageSender;
import ru.samokhin.labCheck.adapter.bot.service.user.teacher.TeacherService;
import ru.samokhin.labCheck.adapter.bot.service.user.teacher.deleteTask.DeleteTaskService;
import ru.samokhin.labCheck.app.api.assignmentGroup.GetAllAssignmentGroupsNameStringInbound;
import ru.samokhin.labCheck.app.api.task.FindTaskByAssignmentGroupInbound;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteTaskHandler implements ProcessHandler {
    private final MessageSender messageSender;
    private final TeacherService teacherService;
    private final DeleteTaskService deleteTaskService;
    private final InlineKeyboardFactory inlineKeyboardFactory;
    private final GetAllAssignmentGroupsNameStringInbound getAllAssignmentGroupsNameStringInbound;
    private final FindTaskByAssignmentGroupInbound findTaskByAssignmentGroupInbound;

    @Override
    public void handleNonCommandUpdate(AbsSender absSender, Message message) {
        Long tgChatId = message.getFrom().getId();

        if (!deleteTaskService.exists(tgChatId)) {
            StatusData statusData = deleteTaskService.startDeletingTask(tgChatId);
            if (!statusData.isSuccess()) {
                messageSender.send(tgChatId, statusData.getMessage(), absSender);
                return;
            }
            askNextQuestion(absSender, tgChatId);
            return;
        }
        messageSender.send(tgChatId, "Неверный способ взаимодействия с ботом, нажмите кнопку", absSender);
    }

    @Override
    public void handleCallbackQuery(AbsSender absSender, CallbackQuery callbackQuery) {
        Long tgChatId = callbackQuery.getFrom().getId();
        String callbackData = callbackQuery.getData();

        switch (callbackData) {
            case "В меню" -> {
                deleteTaskService.removeDeleteTaskData(tgChatId);
                teacherService.removeTeacherData(tgChatId);
                messageSender.send(tgChatId, "Вы вышли в главное меню!", absSender);
                inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
                return;
            }
        }

        StatusData statusData = deleteTaskService.updateState(tgChatId, callbackData);
        if (!statusData.isSuccess()) {
            messageSender.send(tgChatId, statusData.getMessage(), absSender,
                    inlineKeyboardFactory.createSingleButtonKeyboard("В меню", "В меню"));
            inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
            return;
        }
        askNextQuestion(absSender, tgChatId);
        inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);

        if (deleteTaskService.getState(tgChatId) == DeleteTaskState.DELETE_TASK_COMPLETED) {
            deleteTaskService.removeDeleteTaskData(tgChatId);
            teacherService.removeTeacherData(tgChatId);
        }
    }

    private Message askNextQuestion(AbsSender absSender, Long tgChatId) {
        return messageSender.send(tgChatId, formTextForNextQuestion(tgChatId), absSender, formKeyboardForNextQuestion(tgChatId));
    }

    private String formTextForNextQuestion(Long tgChatId) {
        DeleteTaskState state = deleteTaskService.getState(tgChatId);
        return switch (state) {
            case DELETE_TASK_AWAITING_ASSIGNMENT_GROUP -> "Выберите группу из которой хотите удалить задание:";
            case DELETE_TASK_AWAITING_TASK -> "Выберите задание которое хотите удалить";
            case DELETE_TASK_COMPLETED -> "Задача успешно удалена!";
        };
    }

    private ReplyKeyboard formKeyboardForNextQuestion(Long tgChatId) {
        DeleteTaskState state = deleteTaskService.getState(tgChatId);
        return switch (state) {
            case DELETE_TASK_AWAITING_ASSIGNMENT_GROUP -> inlineKeyboardFactory.createColumnKeyboard(getAssignedGroupDataMap());
            case DELETE_TASK_AWAITING_TASK -> inlineKeyboardFactory.createColumnKeyboard(getTestDataMap(tgChatId));
            case DELETE_TASK_COMPLETED -> null;
        };
    }

    private Map<String, String> getAssignedGroupDataMap() {
        List<String> groupNames = getAllAssignmentGroupsNameStringInbound.execute();
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

    private Map<String, String> getTestDataMap(Long tgChatId) {
        LinkedHashMap<String, String> result = findTaskByAssignmentGroupInbound.
                execute(deleteTaskService.getDeleteAssignmentGroup(tgChatId)).stream()
                .collect(Collectors.toMap(
                        Task::getName,
                        Task::getName,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));

        result.put("В меню", "В меню");
        return result;
    }
}
