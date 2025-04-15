package ru.samokhin.labCheck.app.api.assignmentGroup;

import java.util.List;

public interface FindAssignmentGroupsByStudentTgChatIdInbound {
    List<String> execute(Long tgChatId);
}
