package ru.samokhin.labCheck.app.api.python;

public interface PythonUnitTestCheckInbound {
    boolean execute(String studentCode, String testCode);
}