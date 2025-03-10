package ru.samokhin.labCheck.app.api.student;

public interface IsStudentExistsByStudentCardNumberInbound {
    boolean execute(String studentCardNumber);
}
