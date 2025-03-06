package ru.samokhin.labCheck.app.api.teacher;

import ru.samokhin.labCheck.domain.teacher.Teacher;

public interface FindTeacherByTgChatIdInbound {
    Teacher execute(Long tgChatId);
}
