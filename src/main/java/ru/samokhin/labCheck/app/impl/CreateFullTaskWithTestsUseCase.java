package ru.samokhin.labCheck.app.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.scenario.CreateFullTaskWithTestsInbound;
import ru.samokhin.labCheck.app.api.task.TaskRepository;
import ru.samokhin.labCheck.app.api.test.TestRepository;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;
import ru.samokhin.labCheck.domain.teacher.Teacher;
import ru.samokhin.labCheck.domain.test.Test;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateFullTaskWithTestsUseCase implements CreateFullTaskWithTestsInbound {
    private final TaskRepository taskRepository;
    private final TestRepository testRepository;

    @Transactional
    @Override
    public boolean execute(String name, String description, Teacher teacher, AssignmentGroup assignmentGroup,
                           Set<StudentGroup> studentGroups, Set<Test> tests) {
        try {
            Task task = new Task(name, description, teacher, assignmentGroup, studentGroups);
            taskRepository.save(task);

            for (Test test : tests) {
                Test newTest = new Test(test.getName(), test.getScript(), task);
                testRepository.save(newTest);
            }
        } catch (Exception e) {
            throw e;
        }
        return true;
    }
}
