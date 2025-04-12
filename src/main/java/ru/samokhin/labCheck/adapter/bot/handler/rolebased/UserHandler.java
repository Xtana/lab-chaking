package ru.samokhin.labCheck.adapter.bot.handler.rolebased;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;

public interface UserHandler extends ProcessHandler {
    UserRole getRole();
}

