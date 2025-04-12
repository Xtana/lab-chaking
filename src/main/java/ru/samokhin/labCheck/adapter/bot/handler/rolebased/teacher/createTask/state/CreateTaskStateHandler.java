package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createTask.state;

import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskState;

public interface CreateTaskStateHandler {
    StatusData handle(CreateTaskContext context, String input);
    CreateTaskState currantState();
    CreateTaskState nextState();
}
