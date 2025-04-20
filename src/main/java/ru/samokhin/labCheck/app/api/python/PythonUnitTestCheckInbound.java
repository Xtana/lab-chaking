package ru.samokhin.labCheck.app.api.python;

import ru.samokhin.labCheck.app.impl.python.PythonUnitTestResult;

public interface PythonUnitTestCheckInbound {
    PythonUnitTestResult execute(String studentCode, String testCode);
}