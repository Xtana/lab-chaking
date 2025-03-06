package ru.samokhin.labCheck.app.api.teacher;

public interface IsTeacherExistsByTgChatIdInbound {
    boolean execute(Long tgChatId);
}
