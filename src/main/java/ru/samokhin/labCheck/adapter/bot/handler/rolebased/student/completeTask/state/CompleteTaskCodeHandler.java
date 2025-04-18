package ru.samokhin.labCheck.adapter.bot.handler.rolebased.student.completeTask.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskState;
import ru.samokhin.labCheck.app.api.python.PythonSyntaxCheckInbound;
import ru.samokhin.labCheck.app.api.test.FindTestByTaskInbound;
import ru.samokhin.labCheck.domain.task.Task;
import ru.samokhin.labCheck.domain.test.Test;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompleteTaskCodeHandler implements CompleteTaskStateHandler {
    private final FindTestByTaskInbound findTestByTaskInbound;
    private final PythonSyntaxCheckInbound pythonSyntaxCheckInbound;

    @Override
    public StatusData handle(CompleteTaskContext context, String input) {
        input = input.trim();
        if (input.equals("")) {
            return new StatusData(false, "Скрипт не должен быть пустым!");
        }

        if (!pythonSyntaxCheckInbound.execute(input)) {
            return new StatusData(false, "Ошибка компиляции кода");
        }

        Task task = context.getTask();
        List<Test> tests = findTestByTaskInbound.findByTask(task);




        return null;
    }

    @Override
    public CompleteTaskState currantState() {
        return CompleteTaskState.COMPLETE_TASK_AWAITING_CODE_STR;
    }

    @Override
    public CompleteTaskState nextState() {
        return CompleteTaskState.COMPLETE_TASK_STATE_COMPLETE;
    }
}
