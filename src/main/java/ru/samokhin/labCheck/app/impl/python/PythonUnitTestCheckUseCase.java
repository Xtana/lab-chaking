package ru.samokhin.labCheck.app.impl.python;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.app.api.python.PythonUnitTestCheckInbound;

import java.io.*;

@Component
public class PythonUnitTestCheckUseCase implements PythonUnitTestCheckInbound {

    private final String checkerDir = "src/main/resources/checker";

    @Override
    public synchronized boolean execute(String studentCode, String testCode) {
        try {
            writeToFile(checkerDir + "/solution.py", studentCode);
            writeToFile(checkerDir + "/test.py", testCode);

            ProcessBuilder build = new ProcessBuilder("docker", "build", "-f", "dockerfilesTest/Dockerfile", "-t", "python-checker", ".");
            build.directory(new File("."));
            Process buildProcess = build.start();

            if (buildProcess.waitFor() != 0) {
                printErrors(buildProcess);
                return false;
            }

            ProcessBuilder run = new ProcessBuilder("docker", "run", "--rm", "student-check");
            run.directory(new File("."));
            Process runProcess = run.start();

            printOutput(runProcess);

            return runProcess.waitFor() == 0;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void writeToFile(String path, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(content);
        }
    }

    private void printOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String line;
            System.out.println("=== STDOUT ===");
            while ((line = reader.readLine()) != null) System.out.println(line);

            System.out.println("=== STDERR ===");
            while ((line = errorReader.readLine()) != null) System.out.println(line);
        }
    }

    private void printErrors(Process process) throws IOException {
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            System.out.println("=== Docker build errors ===");
            while ((line = errorReader.readLine()) != null) System.out.println(line);
        }
    }
}
