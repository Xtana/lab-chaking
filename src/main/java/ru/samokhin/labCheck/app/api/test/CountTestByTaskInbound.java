package ru.samokhin.labCheck.app.api.test;

import ru.samokhin.labCheck.domain.task.Task;

public interface CountTestByTaskInbound {
    Integer execute(Task task);
}
