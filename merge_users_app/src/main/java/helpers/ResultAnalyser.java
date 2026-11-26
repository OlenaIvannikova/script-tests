package helpers;

import data.ScriptResult;
import data.UserRecord;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ResultAnalyser {
    private static final Path RESULT_FILE = Paths.get(System.getProperty("user.dir"), "full_users.csv");
    private static final int HEADER_LINE_INDEX = 0;
    private static final int LOGIN_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int EMAIL_INDEX = 2;
    private static final int EXPECTED_COLUMNS = 3;

    private static final String EXPECTED_HEADER = "login,name,email";

    public void assertError(ScriptResult result, String expectedError) {
        assertNotNull(result, "Script result cannot be null");
        // Скрипт завершился с ошибкой
        assertFalse(result.getExitCode() == 0, "Expected script execution to fail");
        // Вывод содержит ожидаемое сообщение об ошибке
        assertTrue(result.getOutput().contains(expectedError), "Expected error: " + expectedError + ", but actual output was: " + result.getOutput()
        );
    }

    public void assertCorrectHeader() {
        List<String> lines = readLines();

        String actualHeader = lines.stream()
                .findFirst()
                .orElse("");

        assertEquals(EXPECTED_HEADER, actualHeader, "CSV file contains an incorrect header");
    }

    public void assertContainsOnlyHeader() {
        List<String> lines = readLines();

        assertEquals(1, lines.size(), "Expected result file to contain only the header");
        assertEquals(EXPECTED_HEADER, lines.get(HEADER_LINE_INDEX), "CSV file contains an incorrect header");
    }

    public void assertUser(String login) {
        List<UserRecord> records = readRecords();

        boolean userExists = records.stream()
                .anyMatch(record -> record.getLogin().equals(login));

        assertTrue(userExists, "Expected user not found: " + login);
    }

    public void assertUsers(String... logins) {
        List<UserRecord> records = readRecords();

        Set<String> actualLogins = records.stream()
                .map(UserRecord::getLogin)
                .collect(Collectors.toSet());

        assertTrue(actualLogins.containsAll(Arrays.asList(logins)),
                "Result does not contain all expected users: " + Arrays.toString(logins));
    }

    public void assertUserAppearsExactlyOnce(String login) {
        List<UserRecord> records = readRecords();

        long count = records.stream()
                .filter(record -> record.getLogin().equals(login))
                .count();

        assertEquals(1, count, "Expected user to appear exactly once: " + login);
    }

    public void assertRecordCount(int expectedCount) {
        List<UserRecord> records = readRecords();

        assertEquals(expectedCount, records.size(), "Unexpected number of records in result file");
    }

    public void assertNotContainUser(String login) {
        List<UserRecord> records = readRecords();

        boolean userExists = records.stream()
                .anyMatch(record -> record.getLogin().equals(login));

        assertFalse(userExists, "Unexpected user found: " + login);
    }

    // Чтение файла и обработка всех строк
    private List<UserRecord> readRecords() {
        List<String> lines = readLines();

        if (lines.size() <= HEADER_LINE_INDEX + 1) {
            return List.of();
        }

        return lines.stream()
                .skip(HEADER_LINE_INDEX + 1)
                .filter(line -> !line.isBlank())
                .map(String::trim)
                .map(this::parseRecord)
                .toList();
    }

    // Чтение всех строк выходного файла
    private List<String> readLines() {
        try {
            return Files.readAllLines(RESULT_FILE);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read result file: " + RESULT_FILE, e);
        }
    }

    // Преобразование одной строки CSV в объект UserRecord
    private UserRecord parseRecord(String line) {
        String[] parts = line.split(",", -1);

        validateRecord(parts);

        return new UserRecord(
                parts[LOGIN_INDEX].trim(),
                parts[NAME_INDEX].trim(),
                parts[EMAIL_INDEX].trim());
    }

    private void validateRecord(String[] record) {
        if (record.length != EXPECTED_COLUMNS) {
            throw new IllegalArgumentException("Invalid record format: " + Arrays.toString(record));
        }
    }
}