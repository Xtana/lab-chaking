package ru.samokhin.labCheck.app.api.student;

public interface IsStudentExistsByTgChatIdInbound {
    boolean execute(Long tgChatId);
}
