package ru.samokhin.labCheck.adapter.bot.keyboard.newUser;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.keyboard.InlineKeyboardFactory;
import ru.samokhin.labCheck.adapter.bot.keyboard.InlineKeyboardAbstract;

@Component
public class RegistrationInlineKeyboardBuilder extends InlineKeyboardAbstract {

    public RegistrationInlineKeyboardBuilder(InlineKeyboardFactory inlineKeyboardFactory) {
        super(inlineKeyboardFactory);
    }
}
