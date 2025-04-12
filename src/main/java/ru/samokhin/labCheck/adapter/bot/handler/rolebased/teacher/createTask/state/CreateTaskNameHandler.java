package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createTask.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskState;

@Component
@RequiredArgsConstructor
public class CreateTaskNameHandler implements CreateTaskStateHandler {
    @Override
    public StatusData handle(CreateTaskContext context, String input) {
        input = input.trim();
        if (input.equals("")) {
            return new StatusData(false, "Название задания не может быть пустым");
        }
        context.getTask().setName(input);
        return new StatusData(true, "");
    }

    @Override
    public CreateTaskState nextState() {
        return next();
    }

    @Override
    public CreateTaskState currantState() {
        return CreateTaskState.CREATE_TASK_AWAITING_TASK_NAME;
    }

    private CreateTaskState next() {
        return CreateTaskState.CREATE_TASK_AWAITING_TASK_DESCRIPTION;
    }
}
