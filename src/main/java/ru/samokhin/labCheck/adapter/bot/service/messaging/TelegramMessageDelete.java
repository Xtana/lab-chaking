package ru.samokhin.labCheck.adapter.bot.service.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Slf4j
public class TelegramMessageDelete implements MessageDelete{
    @Override
    public void delete(Long chatId, Integer messageId, AbsSender absSender) {
        try {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(chatId.toString());
            deleteMessage.setMessageId(messageId);
            absSender.execute(deleteMessage);
        } catch (Exception e) {
            log.error("Не удалось удалить сообщение", e);
        }
    }
}
