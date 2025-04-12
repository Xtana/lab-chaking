package ru.samokhin.labCheck.adapter.persistence.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.samokhin.labCheck.app.api.task.TaskRepository;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepository {
    private final TaskJpaRepository taskJpaRepository;

    @Override
    public void save(Task task) {
        taskJpaRepository.save(task);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskJpaRepository.findById(id);
    }
}
