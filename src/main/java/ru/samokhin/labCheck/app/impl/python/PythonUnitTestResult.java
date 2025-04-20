package ru.samokhin.labCheck.app.impl.python;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PythonUnitTestResult {
    private final boolean success;
    private final String logs;
}