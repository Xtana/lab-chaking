package ru.samokhin.labCheck.adapter.bot.service.user.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.samokhin.labCheck.adapter.bot.model.teacher.TeacherState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final Map<Long, TeacherState> teacherStateMap = new ConcurrentHashMap<>();
    private final Map<Long, Boolean> teacherActiveMap = new ConcurrentHashMap<>();

    public boolean exists(Long userId) {
        return  teacherStateMap.containsKey(userId);
    }

    public boolean isActive(Long userId) {
        return  teacherActiveMap.containsKey(userId);
    }

    public void createTeacherState(Long tgChatId, TeacherState teacherState) {
        teacherStateMap.put(tgChatId, teacherState);
    }

    public void activateTeacher(Long tgChatId) {
        teacherActiveMap.put(tgChatId, true);
    }

    public TeacherState getState(Long tgChatId) {
        return teacherStateMap.get(tgChatId);
    }
}
