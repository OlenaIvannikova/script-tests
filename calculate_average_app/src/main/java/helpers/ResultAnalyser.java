package helpers;

import data.AverageRecord;
import data.ScriptResult;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ResultAnalyser {

    private static final Path RESULT_FILE = Paths.get(System.getProperty("user.dir"), "script", "averages.csv");

    private static final int HEADER_LINE_INDEX = 0;
    private static final int USER_INDEX = 0;
    private static final int MONTH_INDEX = 1;
    private static final int AVERAGE_AMOUNT_INDEX = 2;
    private static final int EXPECTED_COLUMNS = 3;

    List<AverageRecord> records;

    public void assertError(ScriptResult result, String expectedError) {
        // скрипт завершился с ошибкой
        assertFalse(result.isSuccess(), "Expected script execution to fail");

        // вывод содержит ожидаемое сообщение об ошибке
        assertTrue(
                result.getOutput().contains(expectedError),
                "Expected error: " + expectedError
                        + ", but actual output was: " + result.getOutput()
        );
    }

    public void assertRecord(String user, String month, BigDecimal expectedAverageAmount) {
        if (records == null) {
            records = readRecords();
        }

        AverageRecord userRecord = records.stream()
                .filter(record -> record.getUser().equals(user))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Record not found for user: " + user
                ));

        assertEquals(month, userRecord.getMonth());
        assertEquals(expectedAverageAmount, userRecord.getAverageAmount());
    }

    // чтение файла и обработка всех строк
    private List<AverageRecord> readRecords() {
        try {
            String content = Files.readString(RESULT_FILE); // читаем весь файл целиком

            // Заменяем \n на реальные переводы строк
            content = content.replace("\\n", "\n");

           // Содержимое lines:
           //  0	user,month,average_spending
           //  1	user1,ALL,800.0
           //  2	user2,ALL,100.0
            List<String> lines = content.lines().toList();

            if (lines.size() <= HEADER_LINE_INDEX + 1) {
                throw new AssertionError("Result file is empty: " + RESULT_FILE);
            }

            return lines.stream()
                    .skip(HEADER_LINE_INDEX + 1) // пропускаем user,month,average_spending строку
                    .filter(line -> !line.isBlank())
                    .map(String::trim)
                    .map(this::parseRecord)
                    .toList();

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Failed to read result file: " + RESULT_FILE, e
            );
        }
    }

    // преобразование одной строки CSV в объект AverageRecord
    private AverageRecord parseRecord(String line) {
        String[] parts = line.split(",");
        validateRecord(parts);
        return new AverageRecord(
                parts[USER_INDEX].trim(),
                parts[MONTH_INDEX].trim(),
                new BigDecimal(parts[AVERAGE_AMOUNT_INDEX].trim())
        );
    }

    private void validateRecord(String[] record) {
        if (record.length != EXPECTED_COLUMNS) {
            throw new IllegalArgumentException(
                    "Invalid record format: " + Arrays.toString(record)
            );
        }
    }
}
