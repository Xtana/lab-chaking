package ru.samokhin.labCheck.adapter.bot.keyboard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InlineKeyboardFactory {

    /**
     * Создает клавиатуру из одной кнопки.
     */
    public InlineKeyboardMarkup createSingleButtonKeyboard(String text, String callbackData) {
        return createKeyboard(List.of(List.of(createButton(text, callbackData))));
    }

    /**
     * Создает клавиатуру из кнопок в один ряд.
     * Принимает Map, где ключ — callbackData, значение — текст кнопки.
     */
    public InlineKeyboardMarkup createRowKeyboard(Map<String, String> callbackDataToTextMap) {
        List<InlineKeyboardButton> buttons = callbackDataToTextMap.entrySet().stream()
                .map(entry -> createButton(entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());

        return createKeyboard(List.of(buttons));
    }

    /**
     * Создает клавиатуру из кнопок, каждая из которых в своей строке.
     * Принимает Map, где ключ — callbackData, значение — текст кнопки.
     */
    public InlineKeyboardMarkup createColumnKeyboard(Map<String, String> callbackDataToTextMap) {
        List<List<InlineKeyboardButton>> buttonRows = callbackDataToTextMap.entrySet().stream()
                .map(entry -> Collections.singletonList(createButton(entry.getValue(), entry.getKey())))
                .collect(Collectors.toList());

        return createKeyboard(buttonRows);
    }

    public void removeInlineKeyboard(AbsSender absSender, CallbackQuery callbackQuery) {
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
        editMarkup.setMessageId(callbackQuery.getMessage().getMessageId());
        editMarkup.setReplyMarkup(null);

        try {
            absSender.execute(editMarkup);
        } catch (Exception e) {
            log.error("Ошибка при удалении inline клавиатуры", e);
        }
    }

    public void removeInlineKeyboard(AbsSender absSender, Message message) {
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup.setChatId(String.valueOf(message.getChatId()));
        editMarkup.setMessageId(message.getMessageId());
        editMarkup.setReplyMarkup(null);

        try {
            absSender.execute(editMarkup);
        } catch (Exception e) {
            log.error("Ошибка при удалении inline клавиатуры", e);
        }
    }

    /** Создает кнопку. */
    private InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    /** Создает клавиатуру. */
    private InlineKeyboardMarkup createKeyboard(List<List<InlineKeyboardButton>> buttonRows) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(buttonRows);
        return markup;
    }
}
