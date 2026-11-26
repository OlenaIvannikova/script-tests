package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.LoginNameMap;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

public class DataGenerator {
    private static final Path DATA_DIR = Paths.get(System.getProperty("user.dir"), "script");

    public static final Path USERS_TXT_FILE = DATA_DIR.resolve("users.txt");
    public static final Path USERS_JSON_FILE = DATA_DIR.resolve("users.json");
    public static final Path USERS_CSV_FILE = DATA_DIR.resolve("users.csv");

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_CSV_HEADER = "login,email%n";

    public void createUsersTxtFile(int num) {
        try {
            Files.createDirectories(DATA_DIR);

            String users = IntStream.rangeClosed(1, num)
                    .mapToObj(i -> "user" + i)
                    .collect(joining(System.lineSeparator()));

            writeNewData(USERS_TXT_FILE, users);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to generate users.txt file", e);
        }
    }

    public void withUsers(String... usernames) {
        try {
            Files.createDirectories(DATA_DIR);

            String users = String.join(System.lineSeparator(), usernames);

            writeNewData(USERS_TXT_FILE, users);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write users file", e);
        }
    }

    public void createUsersCsvFile(int num) {
        try {
            Files.createDirectories(DATA_DIR);

            StringBuilder records = new StringBuilder();
            records.append(USERS_CSV_HEADER.formatted());

            for (int i = 1; i <= num; i++) {
                records.append("user")
                        .append(i)
                        .append(",")
                        .append(generateRandomEmail(i))
                        .append(System.lineSeparator());
            }

            writeNewData(USERS_CSV_FILE, records.toString());

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to generate email records", e);
        }
    }

    public void withEmailRecord(String login, String email) {
        try {
            Files.createDirectories(DATA_DIR);

            if (Files.notExists(USERS_CSV_FILE)) {
                writeNewData(USERS_CSV_FILE, USERS_CSV_HEADER.formatted());
            }

            String record = "%s,%s%n".formatted(login, email);
            appendData(USERS_CSV_FILE, record);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to append email record: " + login, e);
        }
    }

    public void withEmailRecords(Map<String, String> emailRecords) {
        try {
            Files.createDirectories(DATA_DIR);

            if (Files.notExists(USERS_CSV_FILE)) {
                writeNewData(USERS_CSV_FILE, USERS_CSV_HEADER.formatted());
            }

            String records = emailRecords.entrySet().stream()
                    .map(entry -> "%s,%s%n".formatted(entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining());

            appendData(USERS_CSV_FILE, records);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to append email records", e);
        }
    }

    public void withInvalidDelimiter(String login, String email) {
        try {
            Files.createDirectories(DATA_DIR);

            String record = formatInvalidRecord(login, email);
            appendData(USERS_CSV_FILE, record);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to append email record with invalid delimiter", e);
        }
    }

    public void createUsersJsonFile(LoginNameMap... users) {
        Map<String, String> loginToName = new LinkedHashMap<>();

        for (LoginNameMap user : users) {
            loginToName.put(user.getLogin(), user.getName());
        }

        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(USERS_JSON_FILE.toFile(), loginToName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create users.json", e);
        }
    }

    public void createInvalidUsersJsonFile() {
        try {
            Files.writeString(
                    USERS_JSON_FILE,
                    "{\"user1\": \"User1 User1\", \"user2\": \"User2 User2\""
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to create invalid users.json", e);
        }
    }

    // Перезаписываем файл
    private void writeNewData(Path file, String data) throws IOException {
        Files.writeString(
                file,
                data,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    // Добавляем данные в конец файла
    private void appendData(Path file, String data) throws IOException {
        Files.writeString(
                file,
                data,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    public String generateRandomEmail(int i) {
        return "user" + i + "-" + UUID.randomUUID() + "@test.com";
    }

    private String formatInvalidRecord(String login, String email) {
        return "%s;%s%n".formatted(login, email);
    }
}