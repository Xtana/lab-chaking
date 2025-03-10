package ru.samokhin.labCheck.adapter.bot.service.messaging;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface MessageSender {
    void send(Long chatId, String text, AbsSender absSender);
    void send(Long chatId, String text, AbsSender absSender, InlineKeyboardMarkup inlineKeyboardMarkup);
}
