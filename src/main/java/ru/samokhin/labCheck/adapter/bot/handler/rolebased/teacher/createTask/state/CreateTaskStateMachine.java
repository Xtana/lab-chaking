package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createTask.state;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskState;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CreateTaskStateMachine {
    private final Map<CreateTaskState, CreateTaskStateHandler> handlers = new ConcurrentHashMap<>();

    public CreateTaskStateMachine(List<CreateTaskStateHandler> createTaskStateHandlers) {
        for (CreateTaskStateHandler handler : createTaskStateHandlers) {
            handlers.put(handler.currantState(), handler);
        }
    }

    public StatusData processInput(CreateTaskContext context, String input) {
        CreateTaskState currentState = context.getCurrentState();
        CreateTaskStateHandler handler = handlers.get(currentState);
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
