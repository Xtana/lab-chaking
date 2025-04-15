package ru.samokhin.labCheck.adapter.bot.handler.rolebased.student.completeTask.state;

import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskState;

public interface CompleteTaskStateHandler {
    StatusData handle(CompleteTaskContext context, String input);
    CompleteTaskState currantState();
    CompleteTaskState nextState();
}
