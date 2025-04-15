package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.deleteTask.state;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.deleteTask.DeleteTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.deleteTask.DeleteTaskState;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeleteTaskStateMachine {
    private final Map<DeleteTaskState, DeleteTaskStateHandler> handlers = new ConcurrentHashMap<>();

    public DeleteTaskStateMachine(List<DeleteTaskStateHandler> createTaskStateHandlers) {
        for (DeleteTaskStateHandler handler : createTaskStateHandlers) {
            handlers.put(handler.currantState(), handler);
        }
    }

    public StatusData processInput(DeleteTaskContext context, String input) {
        DeleteTaskState currentState = context.getCurrentState();
        DeleteTaskStateHandler handler = handlers.get(currentState);
        if (handler == null) {
            throw new RuntimeException("Неизвестное состояние.");
        }

        StatusData result = handler.handle(context, input);
        if (result.isSuccess()) {
            context.setCurrentState(handler.nexState());
        }
        return result;
    }
}
