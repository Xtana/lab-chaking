package ru.samokhin.labCheck.adapter.bot.service;

import ru.samokhin.labCheck.adapter.bot.model.UserRole;

public interface UserService {
    UserRole getUserRole(Long userId);
}
