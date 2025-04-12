package ru.samokhin.labCheck.app.impl.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.app.api.task.FindTaskByIdInbound;
import ru.samokhin.labCheck.app.api.test.CreateTestInbound;
import ru.samokhin.labCheck.app.api.test.TestRepository;
import ru.samokhin.labCheck.domain.task.Task;
import ru.samokhin.labCheck.domain.test.Test;

@Component
@RequiredArgsConstructor
public class CreateTestUseCase implements CreateTestInbound {
    private final TestRepository testRepository;

    @Override
    public boolean execute(String name, String script, Task task) {
        try {
            Test test = new Test(
                    name,
                    script,
                    task
            );
            // TODO добавить проверку на уникальность name и test
            testRepository.save(test);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
