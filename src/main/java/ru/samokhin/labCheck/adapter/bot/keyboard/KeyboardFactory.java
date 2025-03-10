package ru.samokhin.labCheck.adapter.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KeyboardFactory {

    /**
     * Создает одну кнопку.
     */
    public InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    /**
     * Создает клавиатуру из одной кнопки.
     */
    public InlineKeyboardMarkup createSingleButtonKeyboard(String text, String callbackData) {
        return createKeyboard(Collections.singletonList(Collections.singletonList(createButton(text, callbackData))));
    }

    /**
     * Создает клавиатуру из нескольких кнопок в одной строке.
     */
    public InlineKeyboardMarkup createSingleRowKeyboard(List<InlineKeyboardButton> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        // Преобразуем список кнопок в список строк (каждая кнопка в своей строке)
        List<List<InlineKeyboardButton>> keyboard = buttons.stream()
                .map(Collections::singletonList) // Каждую кнопку оборачиваем в список (это строка)
                .collect(Collectors.toList());

        markup.setKeyboard(keyboard);
        return markup;
    }

    /**
     * Создает клавиатуру из нескольких строк кнопок.
     */
    public InlineKeyboardMarkup createKeyboard(List<List<InlineKeyboardButton>> buttonRows) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(buttonRows);
        return markup;
    }

    /**
     * Создает клавиатуру из Map<String, String>, где ключ — текст, значение — callbackData.
     * Каждая кнопка будет находиться в своей строке.
     */
    public InlineKeyboardMarkup createKeyboardFromMap(Map<String, String> buttonData) {
        List<List<InlineKeyboardButton>> keyboard = buttonData.entrySet().stream()
                .map(entry -> {
                    InlineKeyboardButton button = createButton(entry.getValue(), entry.getKey());
                    return Collections.singletonList(button);
                })
                .collect(Collectors.toList());

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }
}
