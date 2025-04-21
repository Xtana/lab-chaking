package ru.samokhin.labCheck.adapter.bot.service.user.teacher.getResultsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.getResults.state.GetResultsStateMachine;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.getResults.GetResultsContext;
import ru.samokhin.labCheck.adapter.bot.model.teacher.getResults.GetResultsState;
import ru.samokhin.labCheck.app.api.teacher.FindTeacherByTgChatIdInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class GetResultsService {
    private final GetResultsStateMachine stateMachine;
    private final FindTeacherByTgChatIdInbound findTeacherByTgChatIdInbound;

    private final Map<Long, GetResultsContext> getResultsContext = new ConcurrentHashMap<>();

    public boolean exists(Long tgChatId) {
        return getResultsContext.containsKey(tgChatId);
    }

    public StatusData startGettingResults(Long tgChatId) {
        try {
            findTeacherByTgChatIdInbound.execute(tgChatId);
        } catch (EntityNotFoundException e) {
            return new StatusData(false, "Вас нет в базе!");
        } catch (Exception e) {
            return new StatusData(false, "Ошибка на сервере");
        }

        GetResultsContext context = new GetResultsContext(GetResultsState.GET_RESULTS_ASSIGNMENT_GROUP_NAME, null, null);
        getResultsContext.put(tgChatId, context);
        return new StatusData(true, null);
    }

    public StatusData updateState(Long tgChatId, String input) {
        GetResultsContext context = getResultsContext.get(tgChatId);
        if (context == null) {
            throw new RuntimeException("Создание задачи не начато.");
        }
        return stateMachine.processInput(context, input);
    }

    public GetResultsState getState(Long tgChatId) {
        return getResultsContext.get(tgChatId).getCurrentState();
    }

    public AssignmentGroup getAssignmentGroup(Long tgChatId) {
        return getResultsContext.get(tgChatId).getAssignmentGroup();
    }

    public Task getTask(Long tgChatId) {
        return getResultsContext.get(tgChatId).getTask();
    }

    public void removeGetResultsData(Long tgChatId) {
        getResultsContext.remove(tgChatId);
    }

}
