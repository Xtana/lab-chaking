package ru.samokhin.labCheck.app.api.teacher;

public interface CreateTeacherInbound {
    boolean execute (String firstName, String patronymic, String lastName,
                     String email, Long tgChatId);
}
