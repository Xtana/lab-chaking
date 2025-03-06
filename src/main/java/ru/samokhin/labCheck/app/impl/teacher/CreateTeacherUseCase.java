package ru.samokhin.labCheck.app.impl.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.teacher.CreateTeacherInbound;
import ru.samokhin.labCheck.app.api.teacher.TeacherRepository;
import ru.samokhin.labCheck.domain.teacher.Teacher;

@Component
@RequiredArgsConstructor
public class CreateTeacherUseCase implements CreateTeacherInbound {
    private final TeacherRepository teacherRepository;

    @Transactional
    @Override
    public boolean execute(String firstName, String patronymic, String lastName,
                           String email, Long tgChatId) {
       try {
           Teacher teacher = new Teacher(
                   firstName,
                   patronymic,
                   lastName,
                   email,
                   tgChatId
           );
           teacherRepository.save(teacher);
       } catch (Exception e) {
           return false;
       }
       return true;
    }
}
