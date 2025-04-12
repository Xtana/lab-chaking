package ru.samokhin.labCheck.adapter.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

@RequiredArgsConstructor
public abstract class InlineKeyboardAbstract {
    private final InlineKeyboardFactory inlineKeyboardFactory;

    public InlineKeyboardMarkup getSingleInlineButton(String text, String callbackData) {
        return inlineKeyboardFactory.createSingleButtonKeyboard(text, callbackData);
    }

    public InlineKeyboardMarkup getRowInlineButton(Map<String, String> callbackDataToText) {
        return inlineKeyboardFactory.createRowKeyboard(callbackDataToText);
    }

    public InlineKeyboardMarkup getColumnInlineButton(Map<String, String> callbackDataToText) {
        return inlineKeyboardFactory.createColumnKeyboard(callbackDataToText);
    }

    public void removeInlineKeyboard(AbsSender absSender, CallbackQuery callbackQuery){
        inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
    }

    public void removeInlineKeyboard(AbsSender absSender, Message message){
        inlineKeyboardFactory.removeInlineKeyboard(absSender, message);
    }
}
