package ru.samokhin.labCheck.app.api.student;

public interface IsStudentExistsByEmailInbound {
    boolean execute(String email);
}
