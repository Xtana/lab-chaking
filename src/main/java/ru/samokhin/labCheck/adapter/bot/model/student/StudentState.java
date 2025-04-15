package ru.samokhin.labCheck.adapter.bot.model.student;

import java.util.HashMap;
import java.util.Map;

public enum StudentState {
    COMPLETE_TASK("Выполнить задание");

    private final String displayName;

    private static final Map<String, StudentState> DISPLAY_NAME_MAP = new HashMap<>();

    static {
        for (StudentState state : values()) {
            DISPLAY_NAME_MAP.put(state.displayName, state);
        }
    }

    StudentState(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static StudentState fromDisplayName(String displayName) {
        return DISPLAY_NAME_MAP.get(displayName);
    }
}
