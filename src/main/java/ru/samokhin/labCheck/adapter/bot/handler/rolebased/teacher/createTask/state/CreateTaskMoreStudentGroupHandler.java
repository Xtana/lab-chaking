package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createTask.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskState;

@Component
@RequiredArgsConstructor
public class CreateTaskMoreStudentGroupHandler implements CreateTaskStateHandler {
    private CreateTaskState nextState;

    @Override
    public StatusData handle(CreateTaskContext context, String input) {
        input = input.trim();
        switch (input) {
            case "" -> {
                return new StatusData(false, "Ответ не может быть пустым");
            }
            case "Да" -> nextState = CreateTaskState.CREATE_TASK_AWAITING_STUDENT_GROUP;
            case "Нет" -> nextState = CreateTaskState.CREATE_TASK_AWAITING_TEST_NAME;
            default -> {
                return new StatusData(false, "Нет такого варианта ответа");
            }
        }

        return new StatusData(true, "");
    }

    @Override
    public CreateTaskState currantState() {
        return CreateTaskState.CREATE_TASK_AWAITING_MORE_STUDENT_GROUP;
    }

    @Override
    public CreateTaskState nextState() {
        return nextState;
    }
}
