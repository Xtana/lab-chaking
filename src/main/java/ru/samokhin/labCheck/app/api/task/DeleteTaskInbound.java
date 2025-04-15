package ru.samokhin.labCheck.app.api.task;


import ru.samokhin.labCheck.domain.task.Task;

public interface DeleteTaskInbound {
    boolean execute(Task task);
}
