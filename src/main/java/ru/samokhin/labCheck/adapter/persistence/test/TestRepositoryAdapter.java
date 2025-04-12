package ru.samokhin.labCheck.adapter.persistence.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.samokhin.labCheck.app.api.test.TestRepository;
import ru.samokhin.labCheck.domain.test.Test;

@Repository
@RequiredArgsConstructor
public class TestRepositoryAdapter implements TestRepository {
    private final TestJpaRepository testJpaRepository;

    @Override
    public void save(Test test) {
        testJpaRepository.save(test);
    }
}
