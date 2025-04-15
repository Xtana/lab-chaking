package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.deleteTask.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.deleteTask.DeleteTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.deleteTask.DeleteTaskState;
import ru.samokhin.labCheck.app.api.task.DeleteTaskInbound;
import ru.samokhin.labCheck.app.api.task.FindTaskByAssignmentGroupInbound;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteTaskTaskHandler implements DeleteTaskStateHandler {
    private final FindTaskByAssignmentGroupInbound findTaskByAssignmentGroupInbound;
    private final DeleteTaskInbound deleteTaskInbound;

    @Override
    public StatusData handle(DeleteTaskContext context, String input) {
        try {
            input = input.trim();
            if (input.equals("")) {
                return new StatusData(false, "Названее задания не может быть пустым");
            }
            List<Task> tasks = findTaskByAssignmentGroupInbound.execute(context.getAssignmentGroup());
            Task deleteTask = new Task();
            for (Task task : tasks) {
                if (task.getName().equals(input)) {
                    deleteTask = task;
                    break;
                }
            }
            deleteTaskInbound.execute(deleteTask);

        } catch (Exception e) {
            return new StatusData(false, "Ошибка при удалении группы!");
        }
        return new StatusData(true, "");
    }

    @Override
    public DeleteTaskState currantState() {
        return DeleteTaskState.DELETE_TASK_AWAITING_TASK;
    }

    @Override
    public DeleteTaskState nexState() {
        return DeleteTaskState.DELETE_TASK_COMPLETED;
    }
}
