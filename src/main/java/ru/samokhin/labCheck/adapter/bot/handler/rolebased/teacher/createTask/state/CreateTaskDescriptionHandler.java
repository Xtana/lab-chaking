package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createTask.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskState;

@Component
@RequiredArgsConstructor
public class CreateTaskDescriptionHandler implements CreateTaskStateHandler {
    @Override
    public StatusData handle(CreateTaskContext context, String input) {
        input = input.trim();
        context.getTask().setDescription(input);
        return new StatusData(true, "");
    }

    @Override
    public CreateTaskState nextState() {
        return CreateTaskState.CREATE_TASK_AWAITING_STUDENT_GROUP;    }

    @Override
    public CreateTaskState currantState() {
        return CreateTaskState.CREATE_TASK_AWAITING_TASK_DESCRIPTION;
    }
}
