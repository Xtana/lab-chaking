package ru.samokhin.labCheck.adapter.bot.handler.rolebased.student.completeTask.state;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskState;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CompleteTaskStateMachine {
    private final Map<CompleteTaskState, CompleteTaskStateHandler> handlers = new ConcurrentHashMap<>();

    public CompleteTaskStateMachine(List<CompleteTaskStateHandler> completeTaskStateHandlers) {
        for (CompleteTaskStateHandler handler : completeTaskStateHandlers) {
            handlers.put(handler.currantState(), handler);
        }
    }

    public StatusData processInput(CompleteTaskContext context, String input) {
        CompleteTaskState currentState = context.getCurrentState();
        CompleteTaskStateHandler handler = handlers.get(currentState);
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
