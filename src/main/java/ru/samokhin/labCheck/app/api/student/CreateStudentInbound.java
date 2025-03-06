package ru.samokhin.labCheck.app.api.student;

public interface CreateStudentInbound {
    boolean execute(String firstName, String patronymic, String lastName,
                    String groupName, String email, String studentCardNumber, Long tgChatId);
}
