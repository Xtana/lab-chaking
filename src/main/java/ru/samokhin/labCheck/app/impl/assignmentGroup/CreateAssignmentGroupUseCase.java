package ru.samokhin.labCheck.app.impl.assignmentGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samokhin.labCheck.app.api.assignmentGroup.AssignmentGroupRepository;
import ru.samokhin.labCheck.app.api.assignmentGroup.CreateAssignmentGroupInbound;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.teacher.Teacher;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateAssignmentGroupUseCase implements CreateAssignmentGroupInbound {
    private final AssignmentGroupRepository assignmentGroupRepository;

    @Transactional
    @Override
    public boolean execute(String name, String description, Teacher teacher, Set<Student> students) {
        try {
            AssignmentGroup assignmentGroup = new AssignmentGroup(
                    name,
                    description,
                    teacher,
                    students
            );
            // TODO добавить проверку на уникальность всех значений
            assignmentGroupRepository.save(assignmentGroup);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
