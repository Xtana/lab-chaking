package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.getResults.state;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.getResults.GetResultsContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.getResults.GetResultsState;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GetResultsStateMachine {
    private final Map<GetResultsState, GetResultsStateHandler> handlers = new ConcurrentHashMap<>();

    public GetResultsStateMachine(List<GetResultsStateHandler> getResultsStateHandlers) {
        for (GetResultsStateHandler handler : getResultsStateHandlers) {
            handlers.put(handler.currantState(), handler);
        }
    }

    public StatusData processInput(GetResultsContext context, String input) {
        GetResultsState currentState = context.getCurrentState();
        GetResultsStateHandler handler = handlers.get(currentState);
        if (handler == null) {
            throw new RuntimeException("Неизвестное состояние.");
        }

        StatusData result = handler.handle(context, input);
        if (result.isSuccess()) {
            context.setCurrentState(handler.nextState());
        }
        return result;
    }
}
