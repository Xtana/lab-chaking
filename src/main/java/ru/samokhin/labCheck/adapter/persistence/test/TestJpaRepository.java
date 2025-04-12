package ru.samokhin.labCheck.adapter.persistence.test;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samokhin.labCheck.domain.test.Test;

public interface TestJpaRepository extends JpaRepository<Test, Long> {
}
