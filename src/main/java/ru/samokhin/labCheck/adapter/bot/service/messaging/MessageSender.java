package ru.samokhin.labCheck.adapter.bot.service.messaging;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface MessageSender {
    Message send(Long chatId, String text, AbsSender absSender);
    Message send(Long chatId, String text, AbsSender absSender, InlineKeyboardMarkup inlineKeyboardMarkup);
    Message send(Long chatId, String text, AbsSender absSender, ReplyKeyboard replyKeyboard);
}
