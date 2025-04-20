package ru.samokhin.labCheck.app.api.testResult;

import ru.samokhin.labCheck.domain.testResult.TestResult;

public interface TestResultRepository {
    void save(TestResult testResult);
}
