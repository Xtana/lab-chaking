package ru.samokhin.labCheck.adapter.bot.service.newUserRegistration.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationStatusData;
import ru.samokhin.labCheck.app.api.studentGroup.FindStudentGroupByNameIgnoreCaseInbound;
import ru.samokhin.labCheck.domain.student.Student;

@Component
@RequiredArgsConstructor
public class GroupSelectionHandler implements RegistrationStateHandler {
    private final FindStudentGroupByNameIgnoreCaseInbound findStudentGroupByNameIgnoreCaseInbound;

    @Override
    public RegistrationStatusData handle(Student student, String input) {
        if (input == null || input.isBlank()) {
            return new RegistrationStatusData(false, "Группа не может быть пустой!");
        }
        student.setGroupName(findStudentGroupByNameIgnoreCaseInbound.execute(input));
        return new RegistrationStatusData(true, "");
    }

    @Override
    public RegistrationState currantState() {
        return RegistrationState.AWAITING_GROUP_SELECTION;
    }

    @Override
    public RegistrationState nextState() {
        return RegistrationState.COMPLETED;
    }
}