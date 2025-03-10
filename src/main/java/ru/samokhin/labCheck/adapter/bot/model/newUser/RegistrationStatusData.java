package ru.samokhin.labCheck.adapter.bot.model.newUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationStatusData {
    private boolean isSuccess;
    private String message;
}
