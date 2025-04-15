package ru.samokhin.labCheck.adapter.bot.service.user.teacher.deleteTask;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.deleteTask.state.DeleteTaskStateMachine;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.deleteTask.DeleteTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.deleteTask.DeleteTaskState;
import ru.samokhin.labCheck.app.api.teacher.FindTeacherByTgChatIdInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class DeleteTaskService {
    private final DeleteTaskStateMachine stateMachine;
    private final FindTeacherByTgChatIdInbound findTeacherByTgChatIdInbound;

    private final Map<Long, DeleteTaskContext> deleteTaskContext = new ConcurrentHashMap<>();

    public boolean exists(Long userId) {
        return  deleteTaskContext.containsKey(userId);
    }

    public StatusData startDeletingTask(Long tgChatId) {
        try {
            findTeacherByTgChatIdInbound.execute(tgChatId);
        } catch (EntityNotFoundException e) {
            return new StatusData(false, "Вас нет в базе!");
        } catch (Exception e) {
            return new StatusData(false, "Ошибка на сервере");
        }

        DeleteTaskContext context = new DeleteTaskContext(
                DeleteTaskState.DELETE_TASK_AWAITING_ASSIGNMENT_GROUP,
                new AssignmentGroup(),
                new Task()
        );
        deleteTaskContext.put(tgChatId, context);
        return new StatusData(true, null);
    }

    public StatusData updateState(Long tgChatId, String input) {
        DeleteTaskContext context = deleteTaskContext.get(tgChatId);
        return stateMachine.processInput(context, input);
    }

    public DeleteTaskState getState(Long tgChatId) {
        return deleteTaskContext.get(tgChatId).getCurrentState();
    }

    public AssignmentGroup getDeleteAssignmentGroup(Long tgChatId) {
        return deleteTaskContext.get(tgChatId).getAssignmentGroup();
    }

    public void removeDeleteTaskData(Long tgChatId) {
        deleteTaskContext.remove(tgChatId);
    }
}
