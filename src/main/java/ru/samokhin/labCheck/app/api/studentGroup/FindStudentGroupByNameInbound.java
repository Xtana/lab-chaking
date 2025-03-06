package ru.samokhin.labCheck.app.api.studentGroup;

import ru.samokhin.labCheck.domain.studentGroup.StudentGroup;

public interface FindStudentGroupByNameInbound {
    StudentGroup execute(String name);
}
