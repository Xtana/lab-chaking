package ru.samokhin.labCheck.app.api.testResult;

import ru.samokhin.labCheck.domain.submission.Submission;
import ru.samokhin.labCheck.domain.test.Test;
import ru.samokhin.labCheck.domain.testResult.StatusTestResult;

public interface CreateTestResultInbound {
    void save(String log, StatusTestResult status,
                 Test test, Submission submission);
}
