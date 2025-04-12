package ru.samokhin.labCheck.adapter.bot.model.teacher;

import java.util.HashMap;
import java.util.Map;

public enum TeacherState {
    CREATE_TASK("Создать задачу");

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


