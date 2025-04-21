package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.getResults;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.ProcessHandler;
import ru.samokhin.labCheck.adapter.bot.keyboard.InlineKeyboardFactory;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.teacher.getResults.GetResultsState;
import ru.samokhin.labCheck.adapter.bot.service.messaging.MessageSender;
import ru.samokhin.labCheck.adapter.bot.service.user.teacher.TeacherService;
import ru.samokhin.labCheck.adapter.bot.service.user.teacher.getResultsService.GetResultsService;
import ru.samokhin.labCheck.app.api.assignmentGroup.GetAllAssignmentGroupsNameStringInbound;
import ru.samokhin.labCheck.app.api.student.FindAllStudentsByTaskInbound;
import ru.samokhin.labCheck.app.api.student.FindStudentsWithPassedTestCountByTaskInbound;
import ru.samokhin.labCheck.app.api.task.FindTaskByAssignmentGroupInbound;
import ru.samokhin.labCheck.app.api.test.CountTestByTaskInbound;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.studentGroup.StudentPassedTestsDto;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetResultsHandler implements ProcessHandler {
    private final MessageSender messageSender;
    private final GetResultsService getResultsService;
    private final TeacherService teacherService;
    private final InlineKeyboardFactory inlineKeyboardFactory;
    private final GetAllAssignmentGroupsNameStringInbound getAllAssignmentGroupsNameStringInbound;
    private final FindTaskByAssignmentGroupInbound findTaskByAssignmentGroupInbound;
    private final FindStudentsWithPassedTestCountByTaskInbound findStudentsWithPassedTestCountByTaskInbound;
    private final CountTestByTaskInbound countTestByTaskInbound;
    private final FindAllStudentsByTaskInbound findAllStudentsByTaskInbound;

    @Override
    public void handleNonCommandUpdate(AbsSender absSender, Message message) {
        Long tgChatId = message.getFrom().getId();

        if (!getResultsService.exists(tgChatId)) {
            StatusData statusData = getResultsService.startGettingResults(tgChatId);
            if (!statusData.isSuccess()) {
                messageSender.send(tgChatId, statusData.getMessage(), absSender);
                return;
            }
            askNextQuestion(absSender, tgChatId);
            return;
        }
        messageSender.send(tgChatId, "Неверный способ взаимодействия с ботом, нажмите кнопку", absSender);

    }

    @Override
    public void handleCallbackQuery(AbsSender absSender, CallbackQuery callbackQuery) {
        Long tgChatId = callbackQuery.getFrom().getId();
        String callbackData = callbackQuery.getData();

        switch (callbackData) {
            case "В меню" -> {
                getResultsService.removeGetResultsData(tgChatId);
                teacherService.removeTeacherData(tgChatId);
                messageSender.send(tgChatId, "Вы вышли в главное меню!", absSender);
                inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
                return;
            }
        }

        StatusData statusData = getResultsService.updateState(tgChatId, callbackData);
        if (!statusData.isSuccess()) {
            messageSender.send(tgChatId, statusData.getMessage(), absSender,
                    inlineKeyboardFactory.createSingleButtonKeyboard("В меню", "В меню"));
            inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);
            return;
        }
        askNextQuestion(absSender, tgChatId);
        inlineKeyboardFactory.removeInlineKeyboard(absSender, callbackQuery);

        if (getResultsService.getState(tgChatId) == GetResultsState.GET_RESULTS_COMPLETED) {
            getResultsService.removeGetResultsData(tgChatId);
            teacherService.removeTeacherData(tgChatId);
        }
    }

    private Message askNextQuestion(AbsSender absSender, Long tgChatId) {
        return messageSender.send(tgChatId, formTextForNextQuestion(tgChatId), absSender, formKeyboardForNextQuestion(tgChatId));
    }

    private String formTextForNextQuestion(Long tgChatId) {
        GetResultsState state = getResultsService.getState(tgChatId);
        return switch (state) {
            case GET_RESULTS_ASSIGNMENT_GROUP_NAME -> "Выберите учебную группу:";
            case GET_RESULTS_AWAITING_TASK_NAME -> "Выберите задачу:";
            case GET_RESULTS_COMPLETED -> formatStudentsByGroupWithPassedTestStats(getResultsService.getTask(tgChatId));
        };
    }

    private ReplyKeyboard formKeyboardForNextQuestion(Long tgChatId) {
        GetResultsState state = getResultsService.getState(tgChatId);
        return switch (state) {
            case GET_RESULTS_ASSIGNMENT_GROUP_NAME -> inlineKeyboardFactory.createColumnKeyboard(getAssignedGroupDataMap());
            case GET_RESULTS_AWAITING_TASK_NAME -> inlineKeyboardFactory.createColumnKeyboard(getTaskDataMap(tgChatId));
            case GET_RESULTS_COMPLETED -> null;
        };
    }

    private Map<String, String> getAssignedGroupDataMap() {
        List<String> groupNames = getAllAssignmentGroupsNameStringInbound.execute();
        LinkedHashMap<String, String> result = groupNames.stream()
                .collect(Collectors.toMap(
                        name -> name,
                        name -> name,
                        (v1, v2) -> v2,
                        LinkedHashMap::new
                ));

        result.put("В меню", "В меню");
        return result;
    }

    private Map<String, String> getTaskDataMap(Long tgChatId) {
        LinkedHashMap<String, String> result = findTaskByAssignmentGroupInbound.
                execute(getResultsService.getAssignmentGroup(tgChatId)).stream()
                .collect(Collectors.toMap(
                        Task::getName,
                        Task::getName,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));

        result.put("В меню", "В меню");
        return result;
    }

    @Transactional(readOnly = true)
    public String formatStudentsByGroupWithPassedTestStats(Task task) {
        List<Student> allStudents = findAllStudentsByTaskInbound.execute(task);
        List<StudentPassedTestsDto> passedResults = findStudentsWithPassedTestCountByTaskInbound.execute(task);
        Integer totalTests = countTestByTaskInbound.execute(task);

        // Преобразуем в Map<StudentId, passedCount>
        Map<Long, Long> studentIdToPassedCount = passedResults.stream()
                .collect(Collectors.toMap(dto -> dto.getStudent().getId(), StudentPassedTestsDto::getPassedTestCount));

        // Группируем студентов по их студенческим группам
        Map<String, List<Student>> groupedByGroup = allStudents.stream()
                .collect(Collectors.groupingBy(s -> s.getStudentGroup().getName()));

        StringBuilder result = new StringBuilder();

        groupedByGroup.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // сортировка по названию группы
                .forEach(entry -> {
                    result.append(entry.getKey()).append(":\n"); // Название группы

                    entry.getValue().stream()
                            .sorted(Comparator.comparing(Student::getLastName)) // сортировка студентов внутри группы
                            .forEach(student -> {
                                Long passed = studentIdToPassedCount.get(student.getId());
                                if (passed != null) {
                                    result.append(String.format("  %s %s %s %d/%d\n",
                                            student.getLastName(),
                                            student.getFirstName(),
                                            student.getPatronymic() != null ? student.getPatronymic() : "",
                                            passed,
                                            totalTests));
                                } else {
                                    result.append(String.format("  %s %s %s не выполнил\n",
                                            student.getLastName(),
                                            student.getFirstName(),
                                            student.getPatronymic() != null ? student.getPatronymic() : ""));
                                }
                            });

                    result.append("\n");
                });

        return result.toString();
    }

}
