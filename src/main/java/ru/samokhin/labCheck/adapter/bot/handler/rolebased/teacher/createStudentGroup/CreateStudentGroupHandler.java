package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createStudentGroup;

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
import ru.samokhin.labCheck.adapter.bot.service.user.teacher.createStudentGroup.CreateStudentGroupService;
import ru.samokhin.labCheck.app.api.studentGroup.CreateStudentGroupInbound;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateStudentGroupHandler implements ProcessHandler {
    private final MessageSender messageSender;
    private final TeacherService teacherService;
    private final CreateStudentGroupService createStudentGroupService;
    private final CreateStudentGroupInbound createStudentGroupInbound;
    private final InlineKeyboardFactory inlineKeyboardFactory;
    private Message lastMessageForKbDeleting;

    @Override
    public void handleNonCommandUpdate(AbsSender absSender, Message message) {
        Long tgChatId = message.getFrom().getId();
        String messageText = message.getText();

        if (!createStudentGroupService.exists(tgChatId)) {
            StatusData statusData = createStudentGroupService.startCreatingStudentGroup(tgChatId);
            if (!statusData.isSuccess()) {
                lastMessageForKbDeleting = messageSender.send(tgChatId, statusData.getMessage(), absSender,
                        inlineKeyboardFactory.createSingleButtonKeyboard("В меню", "В меню"));
                return;
            }
            lastMessageForKbDeleting = messageSender.send(tgChatId, "Введите название студенческой группы:", absSender,
                    inlineKeyboardFactory.createSingleButtonKeyboard("В меню", "В меню"));
            return;
        }

        messageText = messageText.trim();
        if (messageText.equals("")) {
            inlineKeyboardFactory.removeInlineKeyboard(absSender, lastMessageForKbDeleting);
            lastMessageForKbDeleting = messageSender.send(tgChatId, "Название группы не может быть пустым!", absSender,
                    inlineKeyboardFactory.createSingleButtonKeyboard("В меню", "В меню"));
            return;
        }

        boolean isCreated = createStudentGroupInbound.execute(messageText);
        if (!isCreated) {
            inlineKeyboardFactory.removeInlineKeyboard(absSender, lastMessageForKbDeleting);
            lastMessageForKbDeleting = messageSender.send(tgChatId, "Группа с таким именем уже существует или произошла ошибка при создании.", absSender,
                    inlineKeyboardFactory.createSingleButtonKeyboard("В меню", "В меню"));
            return;
        }
        createStudentGroupService.removeStudentGroupData(tgChatId);
        teacherService.removeTeacherData(tgChatId);
        inlineKeyboardFactory.removeInlineKeyboard(absSender, lastMessageForKbDeleting);
        messageSender.send(tgChatId, "Группа успешно создана!", absSender);
    }

    @Override
    public void handleCallbackQuery(AbsSender absSender, CallbackQuery callbackQuery) {
        Long tgChatId = callbackQuery.getFrom().getId();
        String callbackData = callbackQuery.getData();

        switch (callbackData) {
            case "В меню" -> {
                createStudentGroupService.removeStudentGroupData(tgChatId);
                teacherService.removeTeacherData(tgChatId);
                inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
                messageSender.send(tgChatId, "Вы вышли в главное меню!", absSender);
            }
        }
    }
}
