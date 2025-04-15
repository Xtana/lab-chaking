package ru.samokhin.labCheck.adapter.bot.model.teacher;

import java.util.HashMap;
import java.util.Map;

public enum TeacherState {
    CREATE_ASSIGNMENT_GROUP("Добавить уч группу"),
    DELETE_ASSIGNMENT_GROUP("Удалить уч группу"),
    CREATE_STUDENT_GROUP("Добавить студ группу"),
    DELETE_STUDENT_GROUP("Удалить студ группу"),
    CREATE_TASK("Создать задачу"),
    DELETE_TASK("Удалить задачу");

    private final String displayName;

    private static final Map<String, TeacherState> DISPLAY_NAME_MAP = new HashMap<>();

    static {
        for (TeacherState state : values()) {
            DISPLAY_NAME_MAP.put(state.displayName, state);
        }
    }

    TeacherState(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static TeacherState fromDisplayName(String displayName) {
        return DISPLAY_NAME_MAP.get(displayName);
    }
}


