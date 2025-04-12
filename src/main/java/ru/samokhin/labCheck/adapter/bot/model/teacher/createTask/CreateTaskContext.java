package ru.samokhin.labCheck.adapter.bot.model.teacher.createTask;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.samokhin.labCheck.domain.task.Task;
import ru.samokhin.labCheck.domain.test.Test;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CreateTaskContext {
    private Task task;
    private CreateTaskState currentState;
    private Set<Test> tests;
    private String currentTestName;

    public CreateTaskContext(Task task, CreateTaskState currentState, Set<Test> tests) {
        this.task = task;
        this.currentState= currentState;
        this.tests = tests;
    }
}
