package helpers;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Random;

public class DataGenerator {
    private static final Path DATA_DIR = Paths.get(System.getProperty("user.dir"), "script", "data");
    private static final Path FILE_PATH = DATA_DIR.resolve("transactions.csv");

    private static final Random RANDOM = new Random();

    private static final String HEADER = "user,date,category,amount%n";
    private static final String[] USERS = {"Alice", "Bob", "Charlie", "Tom"};
    private static final String[] CATEGORIES = {"food", "transport", "beauty"};

    private static final int MIN_AMOUNT = 100;
    private static final int MAX_AMOUNT = 2000;
    private static final int YEAR = 2025;

    public void generateRecords(int num) {
        try {
            Files.createDirectories(DATA_DIR);

            StringBuilder records = new StringBuilder();
            records.append(HEADER.formatted());

            for (int i = 0; i < num; i++) {
                records.append(generateRecord());
            }

            writeNewData(records.toString());

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Failed to generate transaction records", e
            );
        }
    }

    public void withRecord(String user, String date, String category, BigDecimal amount
    ) {
        try {
            Files.createDirectories(DATA_DIR);

            String record = formatRecord(user, date, category, amount);
            appendData(record);

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Failed to append transaction record", e
            );
        }
    }

    // Alice,2025-01-15,food,1200.00
    private String generateRecord() {
        String user = USERS[RANDOM.nextInt(USERS.length)];
        String category = CATEGORIES[RANDOM.nextInt(CATEGORIES.length)];
        String date = generateRandomDate().toString();
        BigDecimal amount = BigDecimal.valueOf(MIN_AMOUNT + RANDOM.nextDouble() * (MAX_AMOUNT - MIN_AMOUNT)).setScale(2, RoundingMode.HALF_UP);

        return formatRecord(user, date, category, amount);
    }

    private LocalDate generateRandomDate() {
        int month = RANDOM.nextInt(12) + 1;

        YearMonth yearMonth = YearMonth.of(YEAR, month);
        int day = RANDOM.nextInt(yearMonth.lengthOfMonth()) + 1;

        return yearMonth.atDay(day);
    }

    private String formatRecord(String user, String date, String category, BigDecimal amount) {
        return "%s,%s,%s,%s%n".formatted(
                user,
                date,
                category,
                amount.setScale(2, RoundingMode.HALF_UP)
        );
    }

    // перезаписываем файл
    private void writeNewData(String data) throws IOException {
        Files.writeString(
                FILE_PATH,
                data,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING // Удаляет всё существующее содержимое файла перед записью новых данных
        );
    }

    // добавляем данные в конец файла
    private void appendData(String data) throws IOException {
        Files.writeString(
                FILE_PATH,
                data,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }
}
