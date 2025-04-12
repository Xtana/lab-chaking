package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createTask.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskState;
import ru.samokhin.labCheck.app.api.scenario.CreateFullTaskWithTestsInbound;
import ru.samokhin.labCheck.domain.teacher.Teacher;

@Component
@RequiredArgsConstructor
public class CreateTaskFillDataHandler implements CreateTaskStateHandler {
    private final CreateFullTaskWithTestsInbound createFullTaskWithTestsInbound;

    @Override
    public StatusData handle(CreateTaskContext context, String input) {
        if (input.equals("Подтвердить данные")) {

            try {
                createFullTaskWithTestsInbound.execute(
                        context.getTask().getName(),
                        context.getTask().getDescription(),
                        context.getTask().getTeacher(),
                        context.getTask().getAssignmentGroup(),
                        context.getTask().getStudentGroups(),
                        context.getTests()
                );
            } catch (Exception e) {
                return new StatusData(false,"Ошибка сохранения задачи");
            }
            return new StatusData(true, "");
        }
        return new StatusData(false,"Данные не подтверждены");
    }

    @Override
    public CreateTaskState currantState() {
        return CreateTaskState.CREATE_TASK_FILL_DATA;
    }

    @Override
    public CreateTaskState nextState() {
        return CreateTaskState.CREATE_TASK_COMPLETED;
    }
}
