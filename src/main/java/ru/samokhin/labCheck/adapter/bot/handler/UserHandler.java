package ru.samokhin.labCheck.adapter.bot.handler;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;

public interface UserHandler {
    void handle(AbsSender absSender, Message message);
    UserRole getRole();
}

