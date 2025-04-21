package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.getResults.state;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.getResults.GetResultsContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.getResults.GetResultsState;
import ru.samokhin.labCheck.app.api.assignmentGroup.FindAssignmentGroupByNameIgnoreCaseInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

@Component
@RequiredArgsConstructor
public class GetResultsAssignmentGroupHandler implements GetResultsStateHandler {
    private final FindAssignmentGroupByNameIgnoreCaseInbound
            findAssignmentGroupByNameIgnoreCaseInbound;

    @Override
    public StatusData handle(GetResultsContext context, String input) {
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
    public GetResultsState currantState() {
        return GetResultsState.GET_RESULTS_ASSIGNMENT_GROUP_NAME;
    }

    @Override
    public GetResultsState nextState() {
        return GetResultsState.GET_RESULTS_AWAITING_TASK_NAME;
    }
}
