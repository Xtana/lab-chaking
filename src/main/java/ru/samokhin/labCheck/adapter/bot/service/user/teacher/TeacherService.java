package ru.samokhin.labCheck.adapter.bot.service.user.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.samokhin.labCheck.adapter.bot.model.teacher.TeacherState;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final Map<Long, TeacherState> teacherStateMap = new ConcurrentHashMap<>();
    private final Set<Long> teacherActiveSet = new CopyOnWriteArraySet<>();

    public boolean exists(Long tgChatId) {
        return  teacherStateMap.containsKey(tgChatId);
    }

    public boolean isActive(Long tgChatId) {
        return  teacherActiveSet.contains(tgChatId);
    }

    public void createTeacherState(Long tgChatId, TeacherState teacherState) {
        teacherStateMap.put(tgChatId, teacherState);
    }

    public void activateTeacher(Long tgChatId) {
        teacherActiveSet.add(tgChatId);
    }

    public TeacherState getState(Long tgChatId) {
        return teacherStateMap.get(tgChatId);
    }

    public void removeTeacherData(Long tgChatId) {
        teacherStateMap.remove(tgChatId);
    }
}
