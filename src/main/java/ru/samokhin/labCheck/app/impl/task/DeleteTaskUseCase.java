package ru.samokhin.labCheck.app.impl.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.task.DeleteTaskInbound;
import ru.samokhin.labCheck.app.api.task.TaskRepository;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeleteTaskUseCase implements DeleteTaskInbound {
    private final TaskRepository taskRepository;

    @Transactional
    @Override
    public boolean execute(Task task) {
        Optional<Task> existing = taskRepository.findById(task.getId());
        if (existing.isEmpty()) {
            return false;
        }

        taskRepository.delete(task);
        return true;
    }

}
