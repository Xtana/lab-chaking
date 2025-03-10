package ru.samokhin.labCheck.adapter.bot.strategy;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.UserHandler;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserHandlerStrategy {
    private final Map<UserRole, UserHandler> handlers = new HashMap<>();

    public UserHandlerStrategy(List<UserHandler> userHandlers) {
        for (UserHandler handler : userHandlers) {
            handlers.put(handler.getRole(), handler);
        }
    }

    public UserHandler getHandler(UserRole role) {
        return handlers.getOrDefault(role, handlers.get(UserRole.NEW));
    }
}

