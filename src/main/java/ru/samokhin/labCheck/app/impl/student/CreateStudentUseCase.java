package ru.samokhin.labCheck.app.impl.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.student.CreateStudentInbound;
import ru.samokhin.labCheck.app.api.student.StudentRepository;
import ru.samokhin.labCheck.app.api.studentGroup.FindStudentGroupByNameInbound;
import ru.samokhin.labCheck.domain.student.Student;

@Component
@RequiredArgsConstructor
public class CreateStudentUseCase implements CreateStudentInbound {
    private final StudentRepository studentRepository;
    private final FindStudentGroupByNameInbound findStudentGroupByNameInbound;

    @Transactional
    @Override
    public boolean execute(String firstName, String patronymic, String lastName, String groupName,
                           String email, String studentCardNumber, Long tgChatId) {
        try {
            Student student = new Student(
                    firstName,
                    patronymic,
                    lastName,
                    findStudentGroupByNameInbound.execute(groupName),
                    email,
                    studentCardNumber,
                    tgChatId
            );
            // TODO добавить проверку на уникальность всех зачаний
            studentRepository.save(student);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
