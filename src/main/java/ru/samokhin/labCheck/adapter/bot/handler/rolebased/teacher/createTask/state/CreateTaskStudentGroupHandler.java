package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createTask.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskState;
import ru.samokhin.labCheck.app.api.studentGroup.FindStudentGroupByNameIgnoreCaseInbound;
import ru.samokhin.labCheck.app.api.studentGroup.GetAllStudentGroupNameStringsInbound;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateTaskStudentGroupHandler implements CreateTaskStateHandler {
    private final FindStudentGroupByNameIgnoreCaseInbound findStudentGroupByNameIgnoreCaseInbound;
    private final GetAllStudentGroupNameStringsInbound getAllStudentGroupNameStringsInbound;
    private CreateTaskState nextState;

    @Override
    public StatusData handle(CreateTaskContext context, String input) {
        input = input.trim();
        if (input.equals("")) {
            return new StatusData(false, "Учебная группа не может быть пустой");
        }
        StudentGroup studentGroup;
        try {
            studentGroup = findStudentGroupByNameIgnoreCaseInbound.execute(input);
        } catch (Exception e) {
            return new StatusData(false, "Ошибка выбора учебной группы, начните заново!");
        }
        Set<StudentGroup> contextStudentGroups = context.getTask().getStudentGroups();
        if (contextStudentGroups == null) {
            contextStudentGroups = new HashSet<>();
        }
        contextStudentGroups.add(studentGroup);
        context.getTask().setStudentGroups(contextStudentGroups);
        if (getAllStudentGroupNameStringsInbound.execute().size() == contextStudentGroups.size()) {
            nextState = CreateTaskState.CREATE_TASK_AWAITING_TEST_NAME;
        } else  {
            nextState = CreateTaskState.CREATE_TASK_AWAITING_MORE_STUDENT_GROUP;
        }
        return new StatusData(true, "");
    }

    @Override
    public CreateTaskState nextState() {
        return nextState;
    }

    @Override
    public CreateTaskState currantState() {
        return CreateTaskState.CREATE_TASK_AWAITING_STUDENT_GROUP;
    }
}
