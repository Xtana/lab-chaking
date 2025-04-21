package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.getResults.state;

import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.getResults.GetResultsContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.getResults.GetResultsState;

public interface GetResultsStateHandler {
    StatusData handle(GetResultsContext context, String input);
    GetResultsState currantState();
    GetResultsState nextState();
}
