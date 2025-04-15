package ru.samokhin.labCheck.adapter.bot.service.user.teacher.createTask;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.createTask.CreateTaskState;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createTask.state.CreateTaskStateMachine;
import ru.samokhin.labCheck.app.api.teacher.FindTeacherByTgChatIdInbound;
import ru.samokhin.labCheck.domain.task.Task;
import ru.samokhin.labCheck.domain.teacher.Teacher;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CreateTaskService {
    private final CreateTaskStateMachine stateMachine;
    private final FindTeacherByTgChatIdInbound findTeacherByTgChatIdInbound;

    private final Map<Long, CreateTaskContext> createTaskContext = new ConcurrentHashMap<>();

    public boolean exists(Long tgChatId) {
        return createTaskContext.containsKey(tgChatId);
    }

    public StatusData startCreatingTask(Long tgChatId) {
        Teacher teacher;
        try {
            teacher = findTeacherByTgChatIdInbound.execute(tgChatId);
        } catch (EntityNotFoundException e) {
            return new StatusData(false, "Вас нет в базе!");
        } catch (Exception e) {
            return new StatusData(false, "Ошибка на сервере");
        }

        Task task = new Task();
        task.setTeacher(teacher);
        CreateTaskContext context = new CreateTaskContext(task, CreateTaskState.CREATE_TASK_AWAITING_ASSIGNMENT_GROUP_NAME, new HashSet<>());
        createTaskContext.put(tgChatId, context);
        return new StatusData(true, null);
    }

    public StatusData updateState(Long tgChatId, String input) {
        CreateTaskContext context = createTaskContext.get(tgChatId);
        if (context == null) {
            throw new RuntimeException("Создание задачи не начато.");
        }
        return stateMachine.processInput(context, input);
    }

    public CreateTaskState getState(Long tgChatId) {
        return createTaskContext.get(tgChatId).getCurrentState();
    }

    public CreateTaskContext getContext(Long tgChatId) {
        return createTaskContext.get(tgChatId);
    }

    public void removeCreateTaskData(Long tgChatId) {
        createTaskContext.remove(tgChatId);
    }
}
