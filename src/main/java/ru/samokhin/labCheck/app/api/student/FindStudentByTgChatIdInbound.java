package ru.samokhin.labCheck.app.api.student;

import ru.samokhin.labCheck.domain.student.Student;

public interface FindStudentByTgChatIdInbound {
    Student execute(Long tgChatId);
}
