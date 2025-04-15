package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createTask.state;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskState;
import ru.samokhin.labCheck.app.api.assignmentGroup.FindAssignmentGroupByNameIgnoreCaseInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

@Component
@RequiredArgsConstructor
public class CreateTaskAssignmentGroupHandler implements CreateTaskStateHandler {
    private final FindAssignmentGroupByNameIgnoreCaseInbound
            findAssignmentGroupByNameIgnoreCaseInbound;

    @Override
    public StatusData handle(CreateTaskContext context, String input) {
        input = input.trim();
        if (input.equals("")) {
            return new StatusData(false, "Имя группы не может быть пустым");
        }
        AssignmentGroup assignmentGroup;
        try {
            assignmentGroup = findAssignmentGroupByNameIgnoreCaseInbound.execute(input);
        } catch (EntityNotFoundException e) {
            return new StatusData(false, "Такой группы не существует!");
        } catch (Exception e) {
            return new StatusData(false, "Ошибка при поиске группы!");
        }

        context.getTask().setAssignmentGroup(assignmentGroup);
        return new StatusData(true, "");
    }

    @Override
    public CreateTaskState nextState() {
        return  CreateTaskState.CREATE_TASK_AWAITING_TASK_NAME;
    }

    @Override
    public CreateTaskState currantState() {
        return CreateTaskState.CREATE_TASK_AWAITING_ASSIGNMENT_GROUP_NAME;
    }

}
