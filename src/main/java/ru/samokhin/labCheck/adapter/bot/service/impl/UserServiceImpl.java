package ru.samokhin.labCheck.adapter.bot.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.samokhin.labCheck.adapter.bot.model.UserRole;
import ru.samokhin.labCheck.adapter.bot.service.UserService;
import ru.samokhin.labCheck.app.api.student.IsStudentExistsByTgChatIdInbound;
import ru.samokhin.labCheck.app.api.teacher.IsTeacherExistsByTgChatIdInbound;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final IsTeacherExistsByTgChatIdInbound isTeacherExistsByTgChatIdInbound;
    private final IsStudentExistsByTgChatIdInbound isStudentExistsByTgChatIdInbound;

    @Override
    public UserRole getUserRole(Long tgChatId) {
        if (existsTeacher(tgChatId)) {
            return UserRole.TEACHER;
        } else if (existsStudent(tgChatId)) {
            return UserRole.STUDENT;
        } else {
            return UserRole.NEW;
        }
    }

    private boolean existsTeacher(Long tgChatId) {
        return isTeacherExistsByTgChatIdInbound.execute(tgChatId);
    }

    private boolean existsStudent(Long tgChatId) {
        return isStudentExistsByTgChatIdInbound.execute(tgChatId);
    }
}

