package helpers;

import data.ScriptResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScriptRunner {
    private static final Path PROJECT_DIR = Path.of(System.getProperty("user.dir"));
    private static final Path INPUT_FILE = PROJECT_DIR.resolve("script/data/transactions.csv");
    private static final Path SCRIPT_PATH = PROJECT_DIR.resolve("script/calculate_averages.sh");

    private static final Path BASH_PATH = Path.of(
            "C:/Users/ivann/AppData/Local/Programs/Git/bin/bash.exe"
    );

    public ScriptResult executeScript() {
        return executeScript(INPUT_FILE);
    }

    public ScriptResult executeScript(Path inputFile) {
        // Проверяем, что путь не равен null
        if (inputFile == null) {
            return new ScriptResult(-1, "Input file cannot be null", 0);
        }

        try {
            validateFile(SCRIPT_PATH, "Bash script");

            // ProcessBuilder — класс для создания и настроек внешних процессов операционной системы
            // Запускаем:
            // bash script/calculate_averages.sh \
            // абсолютный путь к transactions.csv
            ProcessBuilder processBuilder = new ProcessBuilder(
                    BASH_PATH.toString(),
                    SCRIPT_PATH.toString(),
                    inputFile.toAbsolutePath().normalize().toString()
            );

            processBuilder.directory(PROJECT_DIR.toFile());

            // Объединяем два потока: stdout (обычный вывод) и stderr (вывод ошибок)
            processBuilder.redirectErrorStream(true);

            long startTime = System.nanoTime();

            // Запускаем процесс
            Process process = processBuilder.start();

            try {
                // Чтение результата Bash-скрипта:
                // process.getInputStream() - поток, из которого получаем вывод внешнего процесса (stdout и stderr)
                String output = new String(
                        process.getInputStream().readAllBytes(), // читаем все доступные байты из потока до его закрытия
                        StandardCharsets.UTF_8  // кодировка, с помощью которой нужно преобразовать байты в текст
                );

                // waitFor() ожидает завершения внешнего процесса
                int exitCode = process.waitFor();

                long executionTimeMillis =
                        (System.nanoTime() - startTime) / 1_000_000;

                return new ScriptResult(exitCode, output, executionTimeMillis);

            } catch (InterruptedException e) {
                process.destroyForcibly(); //   // Принудительно завершает внешний процесс
                Thread.currentThread().interrupt(); // восстанавливаем флаг прерывания

                long executionTimeMillis = (System.nanoTime() - startTime) / 1_000_000;

                return new ScriptResult(-1, "Script execution was interrupted", executionTimeMillis);
            }

        }
        // Если файл отсутствует или процесс не удалось запустить, возвращаем результат с кодом -1
        catch (IOException | IllegalArgumentException e) {
            return new ScriptResult(-1, e.getMessage(), 0);
        }
    }

    private void validateFile(Path file, String description) {
        if (!Files.isRegularFile(file)) {
            throw new IllegalArgumentException(
                    description + " does not exist or is not a file: " + file
            );
        }
    }
}
