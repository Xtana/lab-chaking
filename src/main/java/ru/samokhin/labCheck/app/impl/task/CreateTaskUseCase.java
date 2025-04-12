package ru.samokhin.labCheck.app.impl.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.task.TaskRepository;
import ru.samokhin.labCheck.app.api.task.CreateTaskInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;
import ru.samokhin.labCheck.domain.task.Task;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.teacher.Teacher;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateTaskUseCase implements CreateTaskInbound {
    private final TaskRepository taskRepository;

    @Transactional
    @Override
    public boolean execute(String name, String description, Teacher teacher,
                           AssignmentGroup assignmentGroup, Set<StudentGroup> studentGroups) {
        try {
            Task task = new Task(
                    name,
                    description,
                    teacher,
                    assignmentGroup,
                    studentGroups
            );
            // TODO добавить проверку на уникальность name
            taskRepository.save(task);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
