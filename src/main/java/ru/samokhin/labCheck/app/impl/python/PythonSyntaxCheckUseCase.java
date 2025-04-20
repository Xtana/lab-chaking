package ru.samokhin.labCheck.app.impl.python;

import org.springframework.stereotype.Component;
import ru.samokhin.labCheck.app.api.python.PythonSyntaxCheckInbound;

import java.io.*;

@Component
public class PythonSyntaxCheckUseCase implements PythonSyntaxCheckInbound {

    @Override
    public synchronized boolean execute(String pythonCode) {
        String checkerDir = "src/main/resources/checker";

        try {
            writeToFile(checkerDir + "/solution.py", pythonCode);

            ProcessBuilder builder = new ProcessBuilder("docker", "build", "-f", "docker/syntax/Dockerfile", "-t", "syntax-checker", ".");
            builder.directory(new File("."));

            Process buildProcess = builder.start();
            int exitCode = buildProcess.waitFor();

            if (exitCode != 0) {
                System.out.println("Ошибка сборки Docker-образа");
                return false;
            }

            ProcessBuilder runProcess = new ProcessBuilder("docker", "run", "--rm", "syntax-checker");
            runProcess.directory(new File("."));

            Process checkProcess = runProcess.start();

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(checkProcess.getErrorStream()));
            String line;
            System.out.println("=== Ошибки (stderr) ===");
            while ((line = errorReader.readLine()) != null) {
                System.out.println(line);
            }

            int checkExitCode = checkProcess.waitFor();
            return checkExitCode == 0;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void writeToFile(String path, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(content);
        }
    }
}
