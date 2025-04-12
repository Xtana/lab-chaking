package ru.samokhin.labCheck.adapter.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@RequiredArgsConstructor
public abstract class ReplyKeyboardAbstract {
    private final ReplyKeyboardFactory replyKeyboardFactory;

    public ReplyKeyboardMarkup createKeyboard(List<String> buttonTexts, int buttonsPerRow) {
        return replyKeyboardFactory.createKeyboard(buttonTexts, buttonsPerRow);
    }
}
