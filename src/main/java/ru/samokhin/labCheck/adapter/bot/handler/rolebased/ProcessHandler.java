package ru.samokhin.labCheck.adapter.bot.handler.rolebased;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface ProcessHandler {
    void handleNonCommandUpdate(AbsSender absSender, Message message);
    void handleCallbackQuery(AbsSender absSender, CallbackQuery callbackQuery);
}
