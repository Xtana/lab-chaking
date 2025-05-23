package ru.samokhin.labCheck.adapter.bot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatusData {
    private boolean isSuccess;
    private String message;
}
