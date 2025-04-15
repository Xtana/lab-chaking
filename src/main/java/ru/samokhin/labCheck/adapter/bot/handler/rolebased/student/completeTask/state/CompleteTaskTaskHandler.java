package ru.samokhin.labCheck.adapter.bot.handler.rolebased.student.completeTask.state;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskState;
import ru.samokhin.labCheck.app.api.task.FindTaskByAssignmentGroupAndNameInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.task.Task;

@Component
@RequiredArgsConstructor
public class CompleteTaskTaskHandler implements CompleteTaskStateHandler {
    private final FindTaskByAssignmentGroupAndNameInbound findTaskByAssignmentGroupAndNameInbound;

    @Override
    public StatusData handle(CompleteTaskContext context, String input) {
        input = input.trim();
        if (input.equals("")) {
            return new StatusData(false, "Название задачи не может быть пустым");
        }
        Task task;
        try {
            task = findTaskByAssignmentGroupAndNameInbound.execute(context.getAssignmentGroup(), input);
        } catch (EntityNotFoundException e) {
            return new StatusData(false, "Такой группы не существует!");
        } catch (Exception e) {
            return new StatusData(false, "Ошибка при поиске группы!");
        }

        context.setTask(task);
        return new StatusData(true, "");
    }

    @Override
    public CompleteTaskState currantState() {
        return null;
    }

    @Override
    public CompleteTaskState nextState() {
        return null;
    }
}
