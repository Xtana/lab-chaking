package ru.samokhin.labCheck.adapter.bot.keyboard.teacher;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.keyboard.ReplyKeyboardAbstract;
import ru.samokhin.labCheck.adapter.bot.keyboard.ReplyKeyboardFactory;

@Component
public class TeacherReplyKeyboardBuilder extends ReplyKeyboardAbstract {
    public TeacherReplyKeyboardBuilder(ReplyKeyboardFactory replyKeyboardFactory) {
        super(replyKeyboardFactory);
    }
}
