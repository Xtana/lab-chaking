package ru.samokhin.labCheck.app.api.test;

import ru.samokhin.labCheck.domain.task.Task;
import ru.samokhin.labCheck.domain.test.Test;

import java.util.List;

public interface TestRepository {
    void save(Test test);
    List<Test> findByTask(Task task);
    Integer countByTask(Task task);
}
