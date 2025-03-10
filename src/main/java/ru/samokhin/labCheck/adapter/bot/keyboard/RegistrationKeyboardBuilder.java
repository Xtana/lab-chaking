package ru.samokhin.labCheck.adapter.bot.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.samokhin.labCheck.app.api.studentGroup.StudentGroupRepository;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RegistrationKeyboardBuilder {
    private final KeyboardFactory keyboardFactory;
    private final StudentGroupRepository groupRepository;

    public InlineKeyboardMarkup getRestartButtonKeyboard() {
        return keyboardFactory.createSingleButtonKeyboard("Начать заново", "restart_registration");
    }

    public InlineKeyboardMarkup getConfirmationKeyboard() {
        InlineKeyboardButton confirmButton = keyboardFactory.createButton("Подтвердить данные", "confirm_registration");
        InlineKeyboardButton restartButton = keyboardFactory.createButton("Начать заново", "restart_registration");

        return keyboardFactory.createSingleRowKeyboard(Arrays.asList(confirmButton, restartButton));
    }

    public InlineKeyboardMarkup getGroupSelection() {
        Map<String, String> buttonData = groupRepository.findAll()
                .stream().collect(Collectors.toMap(
                        group -> "select_group_" + group.getName(),
                        StudentGroup::getName
                ));

        return keyboardFactory.createKeyboardFromMap(buttonData);
    }

}
