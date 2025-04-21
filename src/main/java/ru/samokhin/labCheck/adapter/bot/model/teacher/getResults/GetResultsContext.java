package ru.samokhin.labCheck.adapter.bot.model.teacher.getResults;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.task.Task;

@Getter
@Setter
@AllArgsConstructor
public class GetResultsContext {
    private GetResultsState currentState;
    private AssignmentGroup assignmentGroup;
    private Task task;
}
