package ru.samokhin.labCheck.adapter.persistence.assignmentGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.samokhin.labCheck.app.api.assignmentGroup.AssignmentGroupRepository;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

@Repository
@RequiredArgsConstructor
public class AssignmentGroupRepositoryAdapter implements AssignmentGroupRepository {
    private final AssignmentGroupJpaRepository assignmentGroupJpaRepository;

    @Override
    public void save(AssignmentGroup assignmentGroup) {
        assignmentGroupJpaRepository.save(assignmentGroup);
    }
}
