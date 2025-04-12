package ru.samokhin.labCheck.app.api.task;

import ru.samokhin.labCheck.domain.task.Task;

public interface FindTaskByIdInbound {
    Task findById(Long id);
}
