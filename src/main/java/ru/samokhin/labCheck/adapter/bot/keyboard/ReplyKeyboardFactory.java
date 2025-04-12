package ru.samokhin.labCheck.adapter.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplyKeyboardFactory {

    public ReplyKeyboardMarkup createKeyboard(List<String> buttonTexts, int buttonsPerRow) {
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow currentRow = new KeyboardRow();

        for (int i = 0; i < buttonTexts.size(); i++) {
            currentRow.add(new KeyboardButton(buttonTexts.get(i)));

            if ((i + 1) % buttonsPerRow == 0 || i == buttonTexts.size() - 1) {
                rows.add(currentRow);
                currentRow = new KeyboardRow();
            }
        }

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true); // делает клавиатуру компактной
        keyboardMarkup.setKeyboard(rows);
        keyboardMarkup.setOneTimeKeyboard(true);
        return keyboardMarkup;
    }
}
