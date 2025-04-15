package ru.samokhin.labCheck.adapter.bot.handler.rolebased.student.completeTask.state;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskState;
import ru.samokhin.labCheck.app.api.assignmentGroup.FindAssignmentGroupByNameIgnoreCaseInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

@Component
@RequiredArgsConstructor
public class CompleteTaskAssignmentGroupHandler implements CompleteTaskStateHandler {
    private final FindAssignmentGroupByNameIgnoreCaseInbound
            findAssignmentGroupByNameIgnoreCaseInbound;

    @Override
    public StatusData handle(CompleteTaskContext context, String input) {
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

        context.setAssignmentGroup(assignmentGroup);
        return new StatusData(true, "");
    }

    @Override
    public CompleteTaskState currantState() {
        return CompleteTaskState.COMPLETE_TASK_AWAITING_ASSIGNMENT_GROUP_NAME;
    }

    @Override
    public CompleteTaskState nextState() {
        return CompleteTaskState.COMPLETE_TASK_AWAITING_TASK_NAME;
    }
}
