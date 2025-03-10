package ru.samokhin.labCheck.adapter.bot.model;

public enum RegistrationState {
    AWAITING_FIRST_NAME,
    AWAITING_LAST_NAME,
    AWAITING_PATRONYMIC,
    AWAITING_STUDENT_CARD_NUMBER,
    AWAITING_EMAIL,
    AWAITING_GROUP_SELECTION,
    COMPLETED,
    CONFIRM
}
