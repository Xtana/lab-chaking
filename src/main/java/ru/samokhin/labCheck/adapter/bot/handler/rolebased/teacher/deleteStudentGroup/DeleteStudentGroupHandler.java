package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.deleteStudentGroup;

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
import ru.samokhin.labCheck.adapter.bot.service.user.teacher.deleteStudentGroup.DeleteStudentGroupService;
import ru.samokhin.labCheck.app.api.studentGroup.CreateStudentGroupInbound;
import ru.samokhin.labCheck.app.api.studentGroup.DeleteStudentGroupInbound;
import ru.samokhin.labCheck.app.api.studentGroup.GetAllStudentGroupNameStringsInbound;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteStudentGroupHandler implements ProcessHandler {
    private final MessageSender messageSender;
    private final TeacherService teacherService;
    private final DeleteStudentGroupService deleteStudentGroupService;
    private final InlineKeyboardFactory inlineKeyboardFactory;
    private final GetAllStudentGroupNameStringsInbound getAllStudentGroupNameStringsInbound;
    private final DeleteStudentGroupInbound deleteStudentGroupInbound;

    @Override
    public void handleNonCommandUpdate(AbsSender absSender, Message message) {
        Long tgChatId = message.getFrom().getId();

        if (!deleteStudentGroupService.exists(tgChatId)) {
            StatusData statusData = deleteStudentGroupService.startDeletingStudentGroup(tgChatId);
            if (!statusData.isSuccess()) {
                messageSender.send(tgChatId, statusData.getMessage(), absSender,
                        inlineKeyboardFactory.createSingleButtonKeyboard("В меню", "В меню"));
                return;
            }
            messageSender.send(tgChatId, "Выберите группу для удаления:", absSender,
                    inlineKeyboardFactory.createColumnKeyboard(getStudentGroupDataMap()));
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
            boolean isDeleted = deleteStudentGroupInbound.execute(groupName);
            if (!isDeleted) {
                inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
                messageSender.send(tgChatId, "Произошла ошибка при удалении.", absSender,
                        inlineKeyboardFactory.createSingleButtonKeyboard("В меню", "В меню"));
                return;
            }
            deleteStudentGroupService.removeAssignmentGroupData(tgChatId);
            teacherService.removeTeacherData(tgChatId);
            inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
            messageSender.send(tgChatId, "Группа успешно удалена!", absSender);
            return;
        }

        switch (callbackData) {
            case "В меню" -> {
                deleteStudentGroupService.removeAssignmentGroupData(tgChatId);
                teacherService.removeTeacherData(tgChatId);
                inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
                messageSender.send(tgChatId, "Вы вышли в главное меню!", absSender);
            }
            default -> throw new RuntimeException("Неизвестная команда");
        }
    }

    public Map<String, String> getStudentGroupDataMap() {
        Map<String, String> groupNames = getAllStudentGroupNameStringsInbound.execute()
                .stream()
                .collect(Collectors.toMap(
                        name -> "select_group_" + name,
                        name -> name,
                        (v1, v2) -> v2,
                        LinkedHashMap::new
                ));
        groupNames.put("В меню", "В меню");
        return groupNames;
    }
}
