package ru.samokhin.labCheck.adapter.persistence.assignmentGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

import java.util.List;
import java.util.Optional;

public interface AssignmentGroupJpaRepository extends JpaRepository<AssignmentGroup, Long> {
    Optional<AssignmentGroup> findByNameIgnoreCase(String name);

    @Query(value = """
                SELECT DISTINCT ag.name
                FROM ASSIGNMENT_GROUP ag
                JOIN TASK t ON t.assignment_group_id = ag.id
                JOIN TASK_STUDENT_GROUP tsg ON tsg.task_id = t.id
                JOIN STUDENT s ON s.student_group_id = tsg.student_group_id
                WHERE s.tg_chat_id = :tgChatId
            """, nativeQuery = true)
    List<String> findByStudentTgChatId(@Param("tgChatId") Long tgChatId);
}
