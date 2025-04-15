package ru.samokhin.labCheck.adapter.persistence.assignmentGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

import java.util.List;
import java.util.Optional;

public interface AssignmentGroupJpaRepository extends JpaRepository<AssignmentGroup, Long> {
    Optional<AssignmentGroup> findByNameIgnoreCase(String name);
    @Query("""
    SELECT DISTINCT ag.name
    FROM AssignmentGroup ag
    JOIN ag.tasks t
    JOIN t.taskStudentGroups tsg
    JOIN tsg.studentGroup sg
    JOIN sg.students s
    WHERE s.tgChatId = :tgChatId
""")
    List<String> findByStudentTgChatId(@Param("tgChatId") Long tgChatId);

}
