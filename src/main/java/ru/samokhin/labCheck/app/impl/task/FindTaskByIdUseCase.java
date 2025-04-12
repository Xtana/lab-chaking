package ru.samokhin.labCheck.app.impl.task;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.task.FindTaskByIdInbound;
import ru.samokhin.labCheck.app.api.task.TaskRepository;
import ru.samokhin.labCheck.domain.task.Task;

@Component
@RequiredArgsConstructor
public class FindTaskByIdUseCase implements FindTaskByIdInbound {
    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task Not Found with -> id: " + id));
    }
}
