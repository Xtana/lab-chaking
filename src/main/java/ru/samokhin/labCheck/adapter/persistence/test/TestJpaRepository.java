package ru.samokhin.labCheck.adapter.persistence.test;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samokhin.labCheck.domain.task.Task;
import ru.samokhin.labCheck.domain.test.Test;

import java.util.List;

public interface TestJpaRepository extends JpaRepository<Test, Long> {
    List<Test> findByTask(Task task);
}
