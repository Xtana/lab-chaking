package ru.samokhin.labCheck.adapter.bot.model.teacher.deleteTask;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.task.Task;

@Getter
@Setter
@AllArgsConstructor
public class DeleteTaskContext {
    private DeleteTaskState currentState;
    private AssignmentGroup assignmentGroup;
    private Task task;
}
