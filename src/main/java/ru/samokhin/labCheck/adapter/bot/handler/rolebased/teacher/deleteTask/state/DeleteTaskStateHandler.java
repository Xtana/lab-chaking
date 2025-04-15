package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.deleteTask.state;

import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.deleteTask.DeleteTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.deleteTask.DeleteTaskState;

public interface DeleteTaskStateHandler {
    StatusData handle(DeleteTaskContext context, String input);
    DeleteTaskState currantState();
    DeleteTaskState nexState();
}
