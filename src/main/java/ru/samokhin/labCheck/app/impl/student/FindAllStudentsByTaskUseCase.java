package ru.samokhin.labCheck.app.impl.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.student.FindAllStudentsByTaskInbound;
import ru.samokhin.labCheck.app.api.student.StudentRepository;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindAllStudentsByTaskUseCase implements FindAllStudentsByTaskInbound {
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Student> execute(Task task) {
        return studentRepository.findAllByTask(task);
    }
}
