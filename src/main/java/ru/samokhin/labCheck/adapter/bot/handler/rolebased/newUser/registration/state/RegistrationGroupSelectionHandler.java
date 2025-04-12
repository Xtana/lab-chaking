package ru.samokhin.labCheck.adapter.bot.handler.rolebased.newUser.registration.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.app.api.studentGroup.FindStudentGroupByNameIgnoreCaseInbound;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

@Component
@RequiredArgsConstructor
public class RegistrationGroupSelectionHandler implements RegistrationStateHandler {
    private final FindStudentGroupByNameIgnoreCaseInbound findStudentGroupByNameIgnoreCaseInbound;

    @Override
    public StatusData handle(Student student, String input) {
        if (input == null || input.isBlank()) {
            return new StatusData(false, "Группа не может быть пустой!");
        }
        StudentGroup studentGroup;
        try {
            studentGroup = findStudentGroupByNameIgnoreCaseInbound.execute(input);
        } catch (Exception e) {
            return new StatusData(false, "Ошибка выбора учебной группы, начните заново!");
        }
        student.setStudentGroup(studentGroup);
        return new StatusData(true, "");
    }

    @Override
    public RegistrationState currantState() {
        return RegistrationState.REGISTRATION_AWAITING_STUDENT_GROUP;
    }

    @Override
    public RegistrationState nextState() {
        return RegistrationState.REGISTRATION_COMPLETED;
    }
}