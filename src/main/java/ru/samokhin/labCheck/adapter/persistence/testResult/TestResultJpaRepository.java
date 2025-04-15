package ru.samokhin.labCheck.adapter.persistence.testResult;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samokhin.labCheck.domain.testResult.TestResult;

public interface TestResultJpaRepository extends JpaRepository<TestResult, Long> {
}
