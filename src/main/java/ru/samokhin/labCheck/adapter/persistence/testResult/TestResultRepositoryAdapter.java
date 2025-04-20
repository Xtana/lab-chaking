package ru.samokhin.labCheck.adapter.persistence.testResult;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.samokhin.labCheck.app.api.testResult.TestResultRepository;
import ru.samokhin.labCheck.domain.testResult.TestResult;

@Repository
@RequiredArgsConstructor
public class TestResultRepositoryAdapter implements TestResultRepository {
    private final TestResultJpaRepository testResultJpaRepository;

    @Override
    public void save(TestResult testResult) {
        testResultJpaRepository.save(testResult);
    }
}
