package helpers;

import data.ScriptResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ScriptRunner {
    private static final Path PROJECT_DIR = Path.of(System.getProperty("user.dir"));
    public static final Path USERS_TXT_FILE = PROJECT_DIR.resolve("script/users.txt");
    public static final Path USERS_JSON_FILE = PROJECT_DIR.resolve("script/users.json");
    public static final Path USERS_CSV_FILE = PROJECT_DIR.resolve("script/users.csv");

    private static final Path SCRIPT_PATH = PROJECT_DIR.resolve("script/merge_users.sh");

    private static final Path BASH_PATH = Path.of(
            "C:/Users/ivann/AppData/Local/Programs/Git/bin/bash.exe"
    );

    public ScriptResult executeScript() {
        return executeScript(USERS_TXT_FILE, USERS_JSON_FILE, USERS_CSV_FILE);
    }

    public ScriptResult executeScript(Path... inputFiles) {
        if (inputFiles == null) {
            return new ScriptResult(-1, "Input file paths cannot be null");
        }

        try {
            validateFile(SCRIPT_PATH, "Bash script");
            validateFile(BASH_PATH, "Bash executable");

            for (Path inputFile : inputFiles) {
                validateFile(inputFile, "Input file");
            }

            List<String> command = new ArrayList<>();

            command.add(BASH_PATH.toString());
            command.add(SCRIPT_PATH.toString());

            for (Path inputFile : inputFiles) {
                command.add(inputFile.toAbsolutePath().normalize().toString());
            }

            ProcessBuilder processBuilder = new ProcessBuilder(command);

            processBuilder.directory(PROJECT_DIR.toFile());
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            try {
                String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

                int exitCode = process.waitFor();

                return new ScriptResult(exitCode, output);

            } catch (InterruptedException e) {
                process.destroyForcibly();
                Thread.currentThread().interrupt();

                return new ScriptResult(-1, "Script execution was interrupted");
            }

        } catch (IOException | IllegalArgumentException e) {
            return new ScriptResult(-1, e.getMessage());
        }
    }

    private void validateFile(Path filePath, String fileDescription) {
        if (filePath == null) {
            throw new IllegalArgumentException(fileDescription + " path cannot be null");
        }
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException(fileDescription + " not found: " + filePath);
        }
    }
}