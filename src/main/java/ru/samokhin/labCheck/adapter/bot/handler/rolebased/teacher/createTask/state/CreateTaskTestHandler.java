package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createTask.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskState;
import ru.samokhin.labCheck.app.api.python.PythonSyntaxCheckInbound;
import ru.samokhin.labCheck.domain.task.Task;
import ru.samokhin.labCheck.domain.test.Test;

@Component
@RequiredArgsConstructor
public class CreateTaskTestHandler implements CreateTaskStateHandler {
    private final PythonSyntaxCheckInbound pythonSyntaxCheckInbound;

    @Override
    public StatusData handle(CreateTaskContext context, String input) {
        input = input.trim();
        if (input.equals("")) {
            return new StatusData(false, "Тест не может быть пустым");
        }

        if (!pythonSyntaxCheckInbound.execute(input)) {
            return new StatusData(false, "Ошибка компиляции теста");
        }

        context.getTests().add(new Test(
                context.getCurrentTestName(),
                input,
                context.getTask()
        ));
        return new StatusData(true, "");
    }

    @Override
    public CreateTaskState nextState() {
        return CreateTaskState.CREATE_TASK_AWAITING_MORE_TEST;    }

    @Override
    public CreateTaskState currantState() {
        return CreateTaskState.CREATE_TASK_AWAITING_TEST;
    }
}
