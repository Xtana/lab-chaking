package ru.samokhin.labCheck.adapter.bot.handler.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.samokhin.labCheck.adapter.bot.handler.UserHandler;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;

@Service
@AllArgsConstructor
@Slf4j
public class TeacherHandler implements UserHandler {

    @Override
    public void handle(AbsSender absSender, Message message) {
        User user = message.getFrom();
        Long userId = user.getId();
        SendMessage answer = new SendMessage();
        answer.setChatId(userId);

        answer.setText("""
                Привет преподаватель!
                """);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /start command", e);
        }
    }

    @Override
    public UserRole getRole() {
        return UserRole.TEACHER;
    }
}

