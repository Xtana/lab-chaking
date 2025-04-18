package ru.samokhin.labCheck.app.impl.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.test.FindTestByTaskInbound;
import ru.samokhin.labCheck.app.api.test.TestRepository;
import ru.samokhin.labCheck.domain.task.Task;
import ru.samokhin.labCheck.domain.test.Test;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindTestByTaskUseCase implements FindTestByTaskInbound {
    private final TestRepository testRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Test> findByTask(Task task) {
        return testRepository.findByTask(task);
    }
}
