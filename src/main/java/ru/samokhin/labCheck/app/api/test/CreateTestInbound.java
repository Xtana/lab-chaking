package ru.samokhin.labCheck.app.api.test;

import ru.samokhin.labCheck.domain.task.Task;

public interface CreateTestInbound {
    boolean execute(String name, String script, Task task);
}
