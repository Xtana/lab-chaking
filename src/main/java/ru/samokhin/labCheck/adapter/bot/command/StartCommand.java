package ru.samokhin.labCheck.adapter.bot.command;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.samokhin.labCheck.adapter.bot.handler.UserHandlerStrategy;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;
import ru.samokhin.labCheck.adapter.bot.service.UserService;

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
        log.info("User ID: {}", userId);

        UserRole role = userService.getUserRole(userId);
        log.info("Определённая роль: {}", role);

        handlerStrategy.getHandler(role).handle(absSender, message);
    }
}

