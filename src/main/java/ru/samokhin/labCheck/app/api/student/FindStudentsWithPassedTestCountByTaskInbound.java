package ru.samokhin.labCheck.app.api.student;

import ru.samokhin.labCheck.domain.studentGroup.StudentPassedTestsDto;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.List;

public interface FindStudentsWithPassedTestCountByTaskInbound {
    List<StudentPassedTestsDto> execute(Task task);
}
