package ru.samokhin.labCheck.app.impl.testResult;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.testResult.CreateTestResultInbound;
import ru.samokhin.labCheck.app.api.testResult.TestResultRepository;
import ru.samokhin.labCheck.domain.submission.Submission;
import ru.samokhin.labCheck.domain.test.Test;
import ru.samokhin.labCheck.domain.testResult.StatusTestResult;
import ru.samokhin.labCheck.domain.testResult.TestResult;

@Component
@RequiredArgsConstructor
public class CreateTestResultUseCase implements CreateTestResultInbound {
    private final TestResultRepository testResultRepository;

    @Transactional
    @Override
    public void save(String log, StatusTestResult status, Test test, Submission submission) {
        TestResult testResult = new TestResult(log, status, test, submission);
        try {
           testResultRepository.save(testResult);
        } catch (Exception e) {
            throw e;
        }
    }
}
