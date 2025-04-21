package ru.samokhin.labCheck.app.impl.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.student.FindStudentsWithPassedTestCountByTaskInbound;
import ru.samokhin.labCheck.app.api.student.StudentRepository;
import ru.samokhin.labCheck.domain.studentGroup.StudentPassedTestsDto;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindStudentsWithPassedTestCountByTaskUseCase implements
        FindStudentsWithPassedTestCountByTaskInbound {
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<StudentPassedTestsDto> execute(Task task) {
        return studentRepository.findStudentsWithPassedTestCountByTask(task);
    }
}
