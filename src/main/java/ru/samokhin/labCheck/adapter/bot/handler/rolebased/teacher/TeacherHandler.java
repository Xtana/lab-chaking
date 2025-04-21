package ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.UserHandler;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createAssignmentGroup.CreateAssignmentGroupHandler;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createStudentGroup.CreateStudentGroupHandler;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.createTask.CreateTaskHandler;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.deleteAssignmentGroup.DeleteAssignmentGroupHandler;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.deleteStudentGroup.DeleteStudentGroupHandler;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.deleteTask.DeleteTaskHandler;
import ru.samokhin.labCheck.adapter.bot.handler.rolebased.teacher.getResults.GetResultsHandler;
import ru.samokhin.labCheck.adapter.bot.keyboard.ReplyKeyboardFactory;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;
import ru.samokhin.labCheck.adapter.bot.model.teacher.TeacherState;
import ru.samokhin.labCheck.adapter.bot.service.messaging.MessageSender;
import ru.samokhin.labCheck.adapter.bot.service.user.teacher.TeacherService;

import java.util.ArrayList;
import java.util.List;

import static ru.samokhin.labCheck.adapter.bot.model.teacher.TeacherState.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherHandler implements UserHandler {
    private final MessageSender messageSender;
    private final TeacherService teacherService;
    private final ReplyKeyboardFactory replyKeyboardFactory;
    private final CreateTaskHandler createTaskHandler;
    private final CreateAssignmentGroupHandler createAssignmentGroupHandler;
    private final DeleteAssignmentGroupHandler deleteAssignmentGroupHandler;
    private final CreateStudentGroupHandler studentGroupHandler;
    private final DeleteTaskHandler deleteTaskHandler;
    private final DeleteStudentGroupHandler deleteStudentGroupHandler;
    private final GetResultsHandler getResultsHandler;

    private final List<String> REPLY_KEYBOARD_BUTTONS = new ArrayList<>(){{
        add("Добавить уч группу");
        add("Удалить уч группу");
        add("Добавить студ группу");
        add("Удалить студ группу");
        add("Создать задачу");
        add("Удалить задачу");
        add("Получить результаты");
    }};

    @Override
    public UserRole getRole() {
        return UserRole.TEACHER;
    }

    @Override
    public void handleNonCommandUpdate(AbsSender absSender, Message message) {
        Long tgChatId = message.getFrom().getId();
        String text = message.getText();
        System.out.println(tgChatId + " - " + text);

        if (!teacherService.isActive(tgChatId)) {
          teacherService.activateTeacher(tgChatId);
          messageSender.send(tgChatId,"Выбери действие", absSender, replyKeyboardFactory.createKeyboard(REPLY_KEYBOARD_BUTTONS, 2));
          return;
        } else if (!teacherService.exists(tgChatId)) {
            TeacherState teacherSate = null;
            try {
                teacherSate = TeacherState.fromDisplayName(text);
            } catch (Exception e) {
                messageSender.send(tgChatId, "Неизвестная команда, выберите команду на клавиатуре!", absSender);
            }
            switch (teacherSate) {
                case CREATE_ASSIGNMENT_GROUP -> teacherService.createTeacherState(tgChatId, CREATE_ASSIGNMENT_GROUP);
                case DELETE_ASSIGNMENT_GROUP -> teacherService.createTeacherState(tgChatId, DELETE_ASSIGNMENT_GROUP);
                case CREATE_STUDENT_GROUP -> teacherService.createTeacherState(tgChatId, CREATE_STUDENT_GROUP);
                case DELETE_STUDENT_GROUP -> teacherService.createTeacherState(tgChatId, DELETE_STUDENT_GROUP);
                case CREATE_TASK -> teacherService.createTeacherState(tgChatId, CREATE_TASK);
                case DELETE_TASK -> teacherService.createTeacherState(tgChatId, DELETE_TASK);
                case GET_RESULTS -> teacherService.createTeacherState(tgChatId, GET_RESULTS);
            }
        }

        TeacherState teacherState = teacherService.getState(tgChatId);
        switch (teacherState) {
            case CREATE_ASSIGNMENT_GROUP -> createAssignmentGroupHandler.handleNonCommandUpdate(absSender, message);
            case DELETE_ASSIGNMENT_GROUP -> deleteAssignmentGroupHandler.handleNonCommandUpdate(absSender, message);
            case CREATE_STUDENT_GROUP -> studentGroupHandler.handleNonCommandUpdate(absSender, message);
            case DELETE_STUDENT_GROUP -> deleteStudentGroupHandler.handleNonCommandUpdate(absSender, message);
            case CREATE_TASK -> createTaskHandler.handleNonCommandUpdate(absSender, message);
            case DELETE_TASK -> deleteTaskHandler.handleNonCommandUpdate(absSender, message);
            case GET_RESULTS -> getResultsHandler.handleNonCommandUpdate(absSender, message);
        }
    }

    @Override
    public void handleCallbackQuery(AbsSender absSender, CallbackQuery callbackQuery) {
        Long tgChatId = callbackQuery.getFrom().getId();
        String callbackData = callbackQuery.getData();
        System.out.println(tgChatId + " - " + callbackData);

        if (!teacherService.exists(tgChatId)) {
            messageSender.send(tgChatId, "Ошибка обработки запроса", absSender);
        }

        TeacherState teacherState = teacherService.getState(tgChatId);
        switch (teacherState) {
            case CREATE_ASSIGNMENT_GROUP -> createAssignmentGroupHandler.handleCallbackQuery(absSender, callbackQuery);
            case DELETE_ASSIGNMENT_GROUP -> deleteAssignmentGroupHandler.handleCallbackQuery(absSender, callbackQuery);
            case CREATE_STUDENT_GROUP -> studentGroupHandler.handleCallbackQuery(absSender, callbackQuery);
            case DELETE_STUDENT_GROUP -> deleteStudentGroupHandler.handleCallbackQuery(absSender, callbackQuery);
            case CREATE_TASK -> createTaskHandler.handleCallbackQuery(absSender, callbackQuery);
            case DELETE_TASK -> deleteTaskHandler.handleCallbackQuery(absSender, callbackQuery);
            case GET_RESULTS -> getResultsHandler.handleCallbackQuery(absSender, callbackQuery);
        }
    }
}
