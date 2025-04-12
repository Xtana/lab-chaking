package ru.samokhin.labCheck.adapter.bot.keyboard.teacher;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.keyboard.InlineKeyboardFactory;
import ru.samokhin.labCheck.adapter.bot.keyboard.InlineKeyboardAbstract;

@Component
public class CreateTaskInlineKeyboardBuilder extends InlineKeyboardAbstract {
    public CreateTaskInlineKeyboardBuilder(InlineKeyboardFactory inlineKeyboardFactory) {
        super(inlineKeyboardFactory);
    }
}
