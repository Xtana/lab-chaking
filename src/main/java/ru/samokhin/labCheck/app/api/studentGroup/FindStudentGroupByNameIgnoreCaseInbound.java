package ru.samokhin.labCheck.app.api.studentGroup;

import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

public interface FindStudentGroupByNameIgnoreCaseInbound {
    StudentGroup execute(String name);
}
