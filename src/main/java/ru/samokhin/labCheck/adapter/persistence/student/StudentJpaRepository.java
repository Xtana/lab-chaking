package ru.samokhin.labCheck.adapter.persistence.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.samokhin.labCheck.domain.student.Student;
import ru.samokhin.labCheck.domain.studentGroup.StudentPassedTestsDto;
import ru.samokhin.labCheck.domain.task.Task;

import java.util.List;
import java.util.Optional;

public interface StudentJpaRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByTgChatId(Long tgChatId);

    @Query("""
    SELECT s
    FROM Student s
    JOIN FETCH s.studentGroup sg
    WHERE s.studentGroup IN (
        SELECT sg2
        FROM Task t
        JOIN t.studentGroups sg2
        WHERE t = :task
    )
""")
    List<Student> findAllByTask(@Param("task") Task task);

    @Query("""
            SELECT 
                new ru.samokhin.labCheck.domain.studentGroup.StudentPassedTestsDto(s.student, COUNT(tr))
            FROM TestResult tr
            JOIN tr.submission s
            WHERE tr.status = ru.samokhin.labCheck.domain.testResult.StatusTestResult.PASS
            AND s.task = :task
            GROUP BY s.student
            """)
    List<StudentPassedTestsDto> findStudentsWithPassedTestCountByTask(@Param("task") Task task);

    boolean existsByTgChatId(Long tgChatId);

    boolean existsByEmail(String email);

    boolean existsByStudentCardNumber(String studentCardNumber);
}
