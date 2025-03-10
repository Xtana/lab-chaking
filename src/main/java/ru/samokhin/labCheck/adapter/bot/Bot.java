package ru.samokhin.labCheck.adapter.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.samokhin.labCheck.adapter.bot.strategy.UserHandlerStrategy;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;
import ru.samokhin.labCheck.adapter.bot.service.role.UserService;

import java.util.List;


@Service
@Slf4j
public class Bot extends TelegramLongPollingCommandBot {
    private final UserService userService;
    private final UserHandlerStrategy handlerStrategy;
    private final String botUsername;

    public Bot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            UserService userService,
            UserHandlerStrategy handlerStrategy,
            List<IBotCommand> commandList
    ) {
        super(botToken);
        this.botUsername = botUsername;
        this.userService = userService;
        this.handlerStrategy = handlerStrategy;
        commandList.forEach(this::register);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            Long userId = message.getFrom().getId();
            UserRole role = userService.getUserRole(userId);
            handlerStrategy.getHandler(role).handleNonCommandUpdate(this, message);
        } else if (update.hasCallbackQuery()) {
            Long userId = update.getCallbackQuery().getFrom().getId();
            UserRole role = userService.getUserRole(userId);
            handlerStrategy.getHandler(role).handleCallbackQuery(this, update.getCallbackQuery());
        }
    }

}
