package ru.samokhin.labCheck.adapter.bot.handler.rolebased.student.completeTask.state;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskContext;
import ru.samokhin.labCheck.adapter.bot.model.student.completeTask.CompleteTaskState;
import ru.samokhin.labCheck.app.api.python.PythonSyntaxCheckInbound;
import ru.samokhin.labCheck.app.api.python.PythonUnitTestCheckInbound;
import ru.samokhin.labCheck.app.api.student.FindStudentByTgChatIdInbound;
import ru.samokhin.labCheck.app.api.submission.CreateSubmissionInbound;
import ru.samokhin.labCheck.app.api.submission.DeleteByStudentAndTaskInbound;
import ru.samokhin.labCheck.app.api.test.FindTestByTaskInbound;
import ru.samokhin.labCheck.app.api.testResult.CreateTestResultInbound;
import ru.samokhin.labCheck.app.impl.python.PythonUnitTestResult;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.submission.Submission;
import ru.samokhin.labCheck.domain.task.Task;
import ru.samokhin.labCheck.domain.test.Test;
import ru.samokhin.labCheck.domain.testResult.StatusTestResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompleteTaskCodeHandler implements CompleteTaskStateHandler {
    private final FindTestByTaskInbound findTestByTaskInbound;
    private final PythonSyntaxCheckInbound pythonSyntaxCheckInbound;
    private final PythonUnitTestCheckInbound pythonUnitTestCheckInbound;
    private final CreateSubmissionInbound createSubmissionInbound;
    private final FindStudentByTgChatIdInbound findStudentByTgChatIdInbound;
    private final DeleteByStudentAndTaskInbound deleteByStudentAndTaskInbound;
    private final CreateTestResultInbound createTestResultInbound;

    @Override
    public StatusData handle(CompleteTaskContext context, String input) {
        input = input.trim();
        if (input.equals("")) {
            return new StatusData(false, "Скрипт не должен быть пустым!");
        }

        if (!pythonSyntaxCheckInbound.execute(input)) {
            return new StatusData(false, "Ошибка компиляции кода");
        }

        Task task = context.getTask();
        List<Test> tests = findTestByTaskInbound.findByTask(task);
        Student student = findStudentByTgChatIdInbound.execute(context.getTgChatId());

        deleteByStudentAndTaskInbound.execute(student, task);

        Submission submission;
        try {
            submission = createSubmissionInbound.execute(input, task, student);
        } catch (Exception e) {
            return new StatusData(false, "Ошибка добавления кода в базу");
        }

        int passedTestCount = 0;
        for (Test test : tests) {
            PythonUnitTestResult testResult = pythonUnitTestCheckInbound.execute(input, test.getScript());
            if (!testResult.isSuccess()) {
                try {
                    createTestResultInbound.save(testResult.getLogs(), StatusTestResult.FAIl, test, submission);
                } catch (Exception e) {
                    return new StatusData(false, "Ошибка добавления результатов тестов в базу");
                }

                continue;
            }
            try {
                createTestResultInbound.save("", StatusTestResult.PASS, test, submission);
            } catch (Exception e) {
                return new StatusData(false, "Ошибка добавления результатов тестов в базу");
            }
            passedTestCount++;
        }

        context.setPassedTestCount(passedTestCount);
        context.setTotalTestCount(tests.size());
        return new StatusData(true, "");
    }

    @Override
    public CompleteTaskState currantState() {
        return CompleteTaskState.COMPLETE_TASK_AWAITING_CODE_STR;
    }

    @Override
    public CompleteTaskState nextState() {
        return CompleteTaskState.COMPLETE_TASK_STATE_COMPLETE;
    }
}
