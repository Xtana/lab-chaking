package ru.samokhin.labCheck.adapter.bot.service.user.teacher.createStudentGroup;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.samokhin.labCheck.adapter.bot.model.StatusData;
import ru.samokhin.labCheck.app.api.teacher.FindTeacherByTgChatIdInbound;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@RequiredArgsConstructor
public class CreateStudentGroupService {
    private final FindTeacherByTgChatIdInbound findTeacherByTgChatIdInbound;

    private final Set<Long> teacherSet = new CopyOnWriteArraySet<>();

    public boolean exists(Long tgChatId) {
        return teacherSet.contains(tgChatId);
    }

    public StatusData startCreatingStudentGroup(Long tgChatId) {
        try {
            findTeacherByTgChatIdInbound.execute(tgChatId);
        } catch (EntityNotFoundException e) {
            return new StatusData(false, "Вас нет в базе!");
        } catch (Exception e) {
            return new StatusData(false, "Ошибка на сервере");
        }

        teacherSet.add(tgChatId);
        return new StatusData(true, null);
    }

    public void removeStudentGroupData(Long tgChatId) {
        teacherSet.remove(tgChatId);
    }
}
