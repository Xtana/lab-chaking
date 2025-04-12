package ru.samokhin.labCheck.app.api.task;

import ru.samokhin.labCheck.domain.task.Task;

import java.util.Optional;

public interface TaskRepository {
    void save(Task task);
    Optional<Task> findById(Long id);
}