package ru.samokhin.labCheck.adapter.bot.service.messaging;

import org.telegram.telegrambots.meta.bots.AbsSender;

public interface MessageDelete {
    void delete(Long chatId, Integer messageId, AbsSender absSender);
}
