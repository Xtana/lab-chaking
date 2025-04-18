package ru.samokhin.labCheck.adapter.bot.service.user.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.samokhin.labCheck.adapter.bot.model.student.StudentState;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final Map<Long, StudentState> studentStateMap = new ConcurrentHashMap<>();
    private final Set<Long> studentActiveSet = new CopyOnWriteArraySet<>();

    public boolean exists(Long tgChatId) {
        return  studentStateMap.containsKey(tgChatId);
    }

    public boolean isActive(Long tgChatId) {
        return  studentActiveSet.contains(tgChatId);
    }

    public void createStudentState(Long tgChatId, StudentState studentState) {
        studentStateMap.put(tgChatId, studentState);
    }

    public void activateStudent(Long tgChatId) {
        studentActiveSet.add(tgChatId);
    }

    public StudentState getState(Long tgChatId) {
        return studentStateMap.get(tgChatId);
    }

    public void removeStudentData(Long tgChatId) {
        studentStateMap.remove(tgChatId);
    }
}
