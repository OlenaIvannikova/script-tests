package helpers;

import data.LoginRecord;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

public class DataGenerator {
    private static final Path DATA_DIR = Paths.get(System.getProperty("user.dir"), "script");

    public static final Path USERS_FILE = DATA_DIR.resolve("users.txt");
    public static final Path LOGINS_FILE = DATA_DIR.resolve("logins.csv");
    public static final Path BANNED_FILE = DATA_DIR.resolve("banned.json");

    private static final String CSV_HEADER = "login,last_login%n";

    public void createUsersFile(int num) {
        try {
            Files.createDirectories(DATA_DIR);

            String users = IntStream.rangeClosed(1, num)
                    .mapToObj(i -> "user" + i)
                    .collect(joining(System.lineSeparator()));

            writeNewData(USERS_FILE, users);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to generate users.txt file", e);
        }
    }

    public void withUsers(String... usernames) {
        try {
            Files.createDirectories(DATA_DIR);

            String users = String.join(System.lineSeparator(), usernames);

            writeNewData(USERS_FILE, users);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write users file", e);
        }
    }

    public void createLoginsFile(int num) {
        try {
            Files.createDirectories(DATA_DIR);

            StringBuilder records = new StringBuilder();
            records.append(CSV_HEADER.formatted());

            for (int i = 1; i <= num; i++) {
                records.append("user")
                        .append(i)
                        .append(",")
                        .append(LocalDate.now())
                        .append(System.lineSeparator());
            }

            writeNewData(LOGINS_FILE, records.toString());

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to generate login records", e);
        }
    }

    public void withLoginRecord(String login, LocalDate lastLogin) {
        try {
            Files.createDirectories(DATA_DIR);

            if (Files.notExists(LOGINS_FILE)) {
                writeNewData(LOGINS_FILE, CSV_HEADER.formatted());
            }

            String record = "%s,%s%n".formatted(login, lastLogin);
            appendData(LOGINS_FILE, record);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to append login record: " + login, e);
        }
    }

    public void withLoginRecord(String login, String lastLogin) {
        try {
            Files.createDirectories(DATA_DIR);

            if (Files.notExists(LOGINS_FILE)) {
                writeNewData(LOGINS_FILE, CSV_HEADER.formatted());
            }

            String record = "%s,%s%n".formatted(login, lastLogin);
            appendData(LOGINS_FILE, record);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to append login record: " + login, e);
        }
    }

    public void withInvalidDelimiter(String login, LocalDate lastLogin) {
        try {
            Files.createDirectories(DATA_DIR);

            String record = formatInvalidRecord(login, lastLogin);
            appendData(LOGINS_FILE, record);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to append login record with invalid delimiter", e);
        }
    }

    public void createBannedFile(String... usernames) {
        try {
            Files.createDirectories(DATA_DIR);

            String json = Arrays.stream(usernames)
                    .map(user -> "\"" + user + "\"")
                    .collect(joining(", "));

            String content = "[" + json + "]";

            writeNewData(BANNED_FILE, content);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to generate banned users file", e);
        }
    }

    public void createInvalidBannedFile() {
        try {
            Files.createDirectories(DATA_DIR);

            String content = "[\"user1\", \"user2\",]";

            writeNewData(BANNED_FILE, content);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to generate invalid banned users file", e);
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

    private String formatInvalidRecord(String login, LocalDate lastLogin) {
        return "%s;%s%n".formatted(login, lastLogin);
    }
}