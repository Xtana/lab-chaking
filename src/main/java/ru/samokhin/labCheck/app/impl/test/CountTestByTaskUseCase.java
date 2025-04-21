package ru.samokhin.labCheck.app.impl.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.test.CountTestByTaskInbound;
import ru.samokhin.labCheck.app.api.test.TestRepository;
import ru.samokhin.labCheck.domain.task.Task;

@Component
@RequiredArgsConstructor
public class CountTestByTaskUseCase implements CountTestByTaskInbound {
    private final TestRepository testRepository;

    @Transactional(readOnly = true)
    @Override
    public Integer execute(Task task) {
        return testRepository.countByTask(task);
    }
}
