package ru.samokhin.labCheck.adapter.bot.service.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class TelegramMessageSender implements MessageSender {

    @Override
    public void send(Long chatId, String text, AbsSender absSender) {
        send(chatId, text, absSender, null);
    }

    @Override
    public void send(Long chatId, String text, AbsSender absSender, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        if (inlineKeyboardMarkup != null) {
            message.setReplyMarkup(inlineKeyboardMarkup);
        }

        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения", e);
        }
    }
}

