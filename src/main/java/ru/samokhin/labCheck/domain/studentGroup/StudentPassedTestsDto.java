package ru.samokhin.labCheck.domain.studentGroup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.samokhin.labCheck.domain.student.Student;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentPassedTestsDto {
    private Student student;
    private Long passedTestCount;
}
