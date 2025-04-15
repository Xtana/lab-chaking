package ru.samokhin.labCheck.adapter.bot.handler.rolebased.newUser.registration.state;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationContext;
import ru.samokhin.labCheck.adapter.bot.model.newUser.RegistrationState;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RegistrationStateMachine {
    private final Map<RegistrationState, RegistrationStateHandler> handlers = new ConcurrentHashMap<>();

    public RegistrationStateMachine(List<RegistrationStateHandler> registrationStateHandlers) {
        for (RegistrationStateHandler handler : registrationStateHandlers) {
            handlers.put(handler.currantState(), handler);
        }
    }

    public StatusData processInput(RegistrationContext context, String input) {
        RegistrationState current = context.getCurrentState();
        RegistrationStateHandler handler = handlers.get(current);
        if (handler == null) {
            throw new RuntimeException("Неизвестное состояние.");
        }

        StatusData result = handler.handle(context.getStudent(), input);
        if (result.isSuccess()) {
            context.setCurrentState(handler.nextState());
        }
        return result;
    }
}

