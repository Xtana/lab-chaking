package ru.samokhin.labCheck.adapter.bot.handler.rolebased.student;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.UserHandler;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.student.completeTask.CompleteTaskHandler;
import ru.samokhin.labCheck.adapter.bot.keyboard.ReplyKeyboardFactory;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;
import ru.samokhin.labCheck.adapter.bot.model.student.StudentState;
import ru.samokhin.labCheck.adapter.bot.service.messaging.MessageSender;
import ru.samokhin.labCheck.adapter.bot.service.user.student.StudentService;

import java.util.ArrayList;
import java.util.List;

import static ru.samokhin.labCheck.adapter.bot.model.student.StudentState.COMPLETE_TASK;

@Service
@AllArgsConstructor
@Slf4j
public class StudentHandler implements UserHandler {
    private final MessageSender messageSender;
    private final StudentService studentService;
    private final ReplyKeyboardFactory replyKeyboardFactory;
    private final CompleteTaskHandler completeTaskHandler;

    private final List<String> REPLY_KEYBOARD_BUTTONS = new ArrayList<>(){{
        add("Выполнить задание");
    }};

    @Override
    public UserRole getRole() {
        return UserRole.STUDENT;
    }

    @Override
    public void handleNonCommandUpdate(AbsSender absSender, Message message) {
        Long tgChatId = message.getFrom().getId();
        String text = message.getText();
        System.out.println(tgChatId + " - " + text);

        if (!studentService.isActive(tgChatId)) {
            studentService.activateStudent(tgChatId);
            messageSender.send(tgChatId,"Выбери действие", absSender, replyKeyboardFactory.createKeyboard(REPLY_KEYBOARD_BUTTONS, 2));
            return;
        } else if (!studentService.exists(tgChatId)) {
            switch (StudentState.fromDisplayName(text)) {
                case COMPLETE_TASK -> studentService.createStudentState(tgChatId, COMPLETE_TASK);
            }
        }

        StudentState studentState = studentService.getState(tgChatId);
        switch (studentState) {
            case COMPLETE_TASK -> completeTaskHandler.handleNonCommandUpdate(absSender, message);
        }
    }

    @Override
    public void handleCallbackQuery(AbsSender absSender, CallbackQuery callbackQuery) {
        Long tgChatId = callbackQuery.getFrom().getId();
        String callbackData = callbackQuery.getData();
        System.out.println(tgChatId + " - " + callbackData);

        if (!studentService.exists(tgChatId)) {
            messageSender.send(tgChatId, "Ошибка обработки запроса", absSender);
        }

        StudentState studentState = studentService.getState(tgChatId);
        switch (studentState) {
            case COMPLETE_TASK -> completeTaskHandler.handleCallbackQuery(absSender, callbackQuery);
        }
    }
}
