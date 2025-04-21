package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.getResults.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.getResults.GetResultsContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.getResults.GetResultsState;
import ru.samokhin.labCheck.app.api.task.FindTaskByAssignmentGroupInbound;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetResultsTaskHandler implements GetResultsStateHandler {
    private final FindTaskByAssignmentGroupInbound findTaskByAssignmentGroupInbound;

    @Override
    public StatusData handle(GetResultsContext context, String input) {
        try {
            input = input.trim();
            if (input.equals("")) {
                return new StatusData(false, "Названее задания не может быть пустым");
            }
            List<Task> tasks = findTaskByAssignmentGroupInbound.execute(context.getAssignmentGroup());
            for (Task task : tasks) {
                if (task.getName().equals(input)) {
                    context.setTask(task);
                    break;
                }
            }
        } catch (Exception e) {
            return new StatusData(false, "Ошибка при поиске задачи!");
        }
        return new StatusData(true, "");

    }

    @Override
    public GetResultsState currantState() {
        return GetResultsState.GET_RESULTS_AWAITING_TASK_NAME;
    }

    @Override
    public GetResultsState nextState() {
        return GetResultsState.GET_RESULTS_COMPLETED;
    }
}
