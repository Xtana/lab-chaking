package ru.samokhin.labCheck.app.impl.python;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.app.api.python.PythonUnitTestCheckInbound;

import java.io.*;

@Component
public class PythonUnitTestCheckUseCase implements PythonUnitTestCheckInbound {

    private final String checkerDir = "src/main/resources/checker";

    @Override
    public PythonUnitTestResult execute(String studentCode, String testCode) {
        return executeWithLogs(studentCode, testCode);
    }

    public synchronized PythonUnitTestResult executeWithLogs(String studentCode, String testCode) {
        try {
            writeToFile(checkerDir + "/solution.py", studentCode);
            writeToFile(checkerDir + "/test.py", testCode);

            ProcessBuilder build = new ProcessBuilder("docker", "build", "-f", "docker/unitTest/Dockerfile", "-t", "unittest-checker", ".");
            build.directory(new File("."));
            Process buildProcess = build.start();

            if (buildProcess.waitFor() != 0) {
                String buildLogs = getProcessLogs(buildProcess);
                return new PythonUnitTestResult(false, "Ошибка сборки Docker образа:\n" + buildLogs);
            }

            ProcessBuilder run = new ProcessBuilder("docker", "run", "--rm", "unittest-checker");
            run.directory(new File("."));
            Process runProcess = run.start();

            String runLogs = getProcessLogs(runProcess);
            boolean success = runProcess.waitFor() == 0;

            return new PythonUnitTestResult(success, runLogs);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new PythonUnitTestResult(false, "Произошло исключение:\n" + e.getMessage());
        }
    }

    private void writeToFile(String path, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(content);
        }
    }

    private String getProcessLogs(Process process) throws IOException {
        StringBuilder logs = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String line;
            logs.append("=== STDOUT ===\n");
            while ((line = reader.readLine()) != null) {
                logs.append(line).append("\n");
            }

            logs.append("=== STDERR ===\n");
            while ((line = errorReader.readLine()) != null) {
                logs.append(line).append("\n");
            }
        }

        return logs.toString();
    }
}
