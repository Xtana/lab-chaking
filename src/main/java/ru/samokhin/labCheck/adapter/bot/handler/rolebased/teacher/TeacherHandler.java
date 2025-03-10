package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.UserHandler;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;
import ru.samokhin.labCheck.adapter.bot.service.messaging.MessageSender;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherHandler implements UserHandler {
    private final MessageSender messageSender;

    @Override
    public void handleNonCommandUpdate(AbsSender absSender, Message message) {
        User user = message.getFrom();
        Long chatId = user.getId();
        String text = "Привет преподаватель!";
        messageSender.send(chatId, text, absSender);
    }

    @Override
    public void handleCallbackQuery(AbsSender absSender, CallbackQuery callbackQuery) {
    }

    @Override
    public UserRole getRole() {
        return UserRole.TEACHER;
    }
}

