package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createTask.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskState;

@Component
@RequiredArgsConstructor
public class CreateTaskTestNameHandler implements CreateTaskStateHandler {
    @Override
    public StatusData handle(CreateTaskContext context, String input) {
        input = input.trim();
        if (input.equals("")) {
            return new StatusData(false, "Название теста не может быть пустым");
        }
        context.setCurrentTestName(input);
        return new StatusData(true, "");
    }

    @Override
    public CreateTaskState currantState() {
        return CreateTaskState.CREATE_TASK_AWAITING_TEST_NAME;
    }

    @Override
    public CreateTaskState nextState() {
        return CreateTaskState.CREATE_TASK_AWAITING_TEST;
    }
}
