package ru.samokhin.labCheck.adapter.persistence.task;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samokhin.labCheck.domain.task.Task;

public interface TaskJpaRepository extends JpaRepository<Task, Long> {
}
