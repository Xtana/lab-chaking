package ru.samokhin.labCheck.adapter.persistence.assignmentGroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.samokhin.labCheck.app.api.assignmentGroup.AssignmentGroupRepository;
import ru.samokhin.labCheck.domain.assignmentGroup.AssignmentGroup;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AssignmentGroupRepositoryAdapter implements AssignmentGroupRepository {
    private final AssignmentGroupJpaRepository assignmentGroupJpaRepository;

    @Override
    public void save(AssignmentGroup assignmentGroup) {
        assignmentGroupJpaRepository.save(assignmentGroup);
    }

    @Override
    public Optional<AssignmentGroup> findByNameIgnoreCase(String name) {
        return assignmentGroupJpaRepository.findByNameIgnoreCase(name);
    }

    @Override
    public List<AssignmentGroup> findAll() {
        return assignmentGroupJpaRepository.findAll();
    }

    @Override
    public void delete(AssignmentGroup assignmentGroup) {
        assignmentGroupJpaRepository.delete(assignmentGroup);
    }

    @Override
    public List<String> findByStudentTgChatId(Long tgChatId) {
        return assignmentGroupJpaRepository.findByStudentTgChatId(tgChatId);
    }

}
