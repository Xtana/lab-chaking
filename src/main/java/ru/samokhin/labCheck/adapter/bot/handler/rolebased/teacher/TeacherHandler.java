package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.UserHandler;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createTask.CreateTaskHandler;
import ru.samokhin.labCheck.adapter.bot.keyboard.teacher.TeacherReplyKeyboardBuilder;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;
import ru.samokhin.labCheck.adapter.bot.model.teacher.TeacherState;
import ru.samokhin.labCheck.adapter.bot.service.messaging.MessageSender;
import ru.samokhin.labCheck.adapter.bot.service.user.teacher.TeacherService;

import java.util.ArrayList;
import java.util.List;

import static ru.samokhin.labCheck.adapter.bot.model.teacher.TeacherState.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherHandler implements UserHandler {
    private final MessageSender messageSender;
    private final TeacherService teacherService;
    private final CreateTaskHandler createTaskHandler;
    private final TeacherReplyKeyboardBuilder replyKeyboardBuilder;

    private final List<String> REPLY_KEYBOARD_BUTTONS = new ArrayList<>(){{
        add("Создать задачу");
        add("Удалить задачу");
    }};

    @Override
    public UserRole getRole() {
        return UserRole.TEACHER;
    }

    @Override
    public void handleNonCommandUpdate(AbsSender absSender, Message message) {
        Long tgChatId = message.getFrom().getId();
        String text = message.getText();
        System.out.println(tgChatId + " - " + text);

        if (!teacherService.isActive(tgChatId)) {
          teacherService.activateTeacher(tgChatId);
          messageSender.send(tgChatId,"Сделайте выбор", absSender, replyKeyboardBuilder.createKeyboard(REPLY_KEYBOARD_BUTTONS, 2));
          return;
        } else if (!teacherService.exists(tgChatId)) {
            switch (TeacherState.fromDisplayName(text)) {
                case CREATE_TASK -> teacherService.createTeacherState(tgChatId, CREATE_TASK);
            }
        }

        TeacherState teacherState = teacherService.getState(tgChatId);
        switch (teacherState) {
            case CREATE_TASK -> {
                createTaskHandler.handleNonCommandUpdate(absSender, message);
            }
        }
    }

    @Override
    public void handleCallbackQuery(AbsSender absSender, CallbackQuery callbackQuery) {
        Long tgChatId = callbackQuery.getFrom().getId();
        String callbackData = callbackQuery.getData();
        System.out.println(tgChatId + " - " + callbackData);

        if (!teacherService.exists(tgChatId)) {
            messageSender.send(tgChatId, "Ошибка обработки запроса", absSender);
        }

        TeacherState teacherState = teacherService.getState(tgChatId);
        switch (teacherState) {
            case CREATE_TASK -> createTaskHandler.handleCallbackQuery(absSender, callbackQuery);
        }

    }
}
