package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.deleteAssignmentGroup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.ProcessHandler;
import ru.samokhin.labCheck.adapter.bot.keyboard.InlineKeyboardFactory;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.service.messaging.MessageSender;
import ru.samokhin.labCheck.adapter.bot.service.user.teacher.TeacherService;
import ru.samokhin.labCheck.adapter.bot.service.user.teacher.deleteAssignmentGroupService.DeleteAssignmentGroupService;
import ru.samokhin.labCheck.app.api.assignmentGroup.DeleteAssignmentGroupByNameInbound;
import ru.samokhin.labCheck.app.api.assignmentGroup.GetAllAssignmentGroupsNameStringInbound;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteAssignmentGroupHandler implements ProcessHandler {
    private final MessageSender messageSender;
    private final TeacherService teacherService;
    private final DeleteAssignmentGroupService deleteAssignmentGroupService;
    private final InlineKeyboardFactory inlineKeyboardFactory;
    private final GetAllAssignmentGroupsNameStringInbound getAllAssignmentGroupsNameStringInbound;
    private final DeleteAssignmentGroupByNameInbound deleteAssignmentGroupByNameInbound;

    @Override
    public void handleNonCommandUpdate(AbsSender absSender, Message message) {
        Long tgChatId = message.getFrom().getId();

        if (!deleteAssignmentGroupService.exists(tgChatId)) {
            StatusData statusData = deleteAssignmentGroupService.startDeletingAssignmentGroup(tgChatId);
            if (!statusData.isSuccess()) {
                messageSender.send(tgChatId, statusData.getMessage(), absSender,
                        inlineKeyboardFactory.createSingleButtonKeyboard("В меню", "В меню"));
                return;
            }
            messageSender.send(tgChatId, "Выберите группу для удаления:", absSender,
                    inlineKeyboardFactory.createColumnKeyboard(getAssignedGroupDataMap()));
            return;
        }
        messageSender.send(tgChatId, "Неверный способ взаимодействия с ботом, используйте кнопки!", absSender);
    }

    @Override
    public void handleCallbackQuery(AbsSender absSender, CallbackQuery callbackQuery) {
        Long tgChatId = callbackQuery.getFrom().getId();
        String callbackData = callbackQuery.getData();

        if (callbackData.startsWith("select_group_")) {
            String groupName = callbackData.replace("select_group_", "");
            boolean isDeleted = deleteAssignmentGroupByNameInbound.execute(groupName);
            if (!isDeleted) {
                inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
                messageSender.send(tgChatId, "Произошла ошибка при удалении.", absSender,
                        inlineKeyboardFactory.createSingleButtonKeyboard("В меню", "В меню"));
                return;
            }
            deleteAssignmentGroupService.removeAssignmentGroupData(tgChatId);
            teacherService.removeTeacherData(tgChatId);
            inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
            messageSender.send(tgChatId, "Группа успешно удалена!", absSender);
            return;
        }

        switch (callbackData) {
            case "В меню" -> {
                deleteAssignmentGroupService.removeAssignmentGroupData(tgChatId);
                teacherService.removeTeacherData(tgChatId);
                inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
                messageSender.send(tgChatId, "Вы вышли в главное меню!", absSender);
            }
            default -> throw new RuntimeException("Неизвестная команда");
        }
    }

    private Map<String, String> getAssignedGroupDataMap() {
        List<String> groupNames = getAllAssignmentGroupsNameStringInbound.execute();
        LinkedHashMap<String, String> result = groupNames.stream()
                .collect(Collectors.toMap(
                        name -> "select_group_" + name,
                        name -> name,
                        (v1, v2) -> v2,
                        LinkedHashMap::new
                ));

        result.put("В меню", "В меню");
        return result;
    }

}
