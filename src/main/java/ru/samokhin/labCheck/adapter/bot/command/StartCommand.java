package ru.samokhin.labCheck.adapter.bot.command;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.strategy.UserHandlerStrategy;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;
import ru.samokhin.labCheck.adapter.bot.service.role.UserService;

@Service
@AllArgsConstructor
@Slf4j
public class StartCommand implements IBotCommand {
    private final UserService userService;
    private final UserHandlerStrategy handlerStrategy;

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запускает бота";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        User user = message.getFrom();
        Long userId = user.getId();
        UserRole role = userService.getUserRole(userId);

        log.info("User ID: {}", userId);
        log.info("Определённая роль: {}", role);

        handlerStrategy.getHandler(role).handleNonCommandUpdate(absSender, message);
    }
}

