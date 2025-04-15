package ru.samokhin.labCheck.adapter.bot.service.user.student.completeTask;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.student.completeTask.state.CompleteTaskStateMachine;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskState;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CompleteTaskService {
    private final CompleteTaskStateMachine stateMachine;

    private final Map<Long, CompleteTaskContext> completeTaskContext = new ConcurrentHashMap<>();

    public boolean exists(Long tgChatId) {
        return completeTaskContext.containsKey(tgChatId);
    }

    public void startCompletingTask(Long tgChatId) {
        CompleteTaskContext context = new CompleteTaskContext(
                CompleteTaskState.COMPLETE_TASK_AWAITING_ASSIGNMENT_GROUP_NAME,
                new AssignmentGroup(),
                new Task()
                );
        completeTaskContext.put(tgChatId, context);
    }

    public StatusData updateState(Long tgChatId, String input) {
        CompleteTaskContext context = completeTaskContext.get(tgChatId);
        if (context == null) {
            throw new RuntimeException("Выполнение задачи не начато.");
        }
        return stateMachine.processInput(context, input);
    }

    public CompleteTaskState getState(Long tgChatId) {
        return completeTaskContext.get(tgChatId).getCurrentState();
    }

    public CompleteTaskContext getContext(Long tgChatId) {
        return completeTaskContext.get(tgChatId);
    }

    public void removeCompletingTaskData(Long tgChatId) {
        completeTaskContext.remove(tgChatId);
    }
}
