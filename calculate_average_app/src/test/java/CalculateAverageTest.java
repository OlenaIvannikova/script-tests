import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Path;

public class CalculateAverageTest {

    @Test
    public void calculatesAverageCorrectlyForTwoUsersWithOneTransactionEach() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withRecord("user1", "2025-01-01", "transport", BigDecimal.valueOf(800.00))
                .withRecord("user2", "2025-01-02", "food", BigDecimal.valueOf(100.00))
                .when()
                .executeScript()
                .then()
                .containsRecord("user1", "ALL", BigDecimal.valueOf(800.00))
                .containsRecord("user2", "ALL", BigDecimal.valueOf(100.00));
    }

    @Test
    public void calculatesAverageCorrectlyForOneUserWithOneTransaction() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withRecord("user1", "2025-01-01", "transport", BigDecimal.valueOf(800.00))
                .when()
                .executeScript()
                .then()
                .containsRecord("user1", "ALL", BigDecimal.valueOf(800.00));
    }

    @Test
    public void calculatesAverageCorrectlyForOneUserWithMultipleTransactionsInOneMonth() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withRecord("user1", "2025-01-01", "transport", BigDecimal.valueOf(600.00))
                .withRecord("user1", "2025-01-02", "food", BigDecimal.valueOf(200.00))
                .withRecord("user1", "2025-01-03", "beauty", BigDecimal.valueOf(100.00))
                .when()
                .executeScript()
                .then()
                .containsRecord("user1", "ALL", BigDecimal.valueOf(300.00));
    }

    @Test
    public void calculatesAverageCorrectlyForOneUserWithOneTransactionInDifferentMonths() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withRecord("user1", "2025-01-01", "food", BigDecimal.valueOf(100.00))
                .withRecord("user1", "2025-02-01", "food", BigDecimal.valueOf(200.00))
                .withRecord("user1", "2025-03-01", "food", BigDecimal.valueOf(300.00))
                .when()
                .executeScript()
                .then()
                .containsRecord("user1", "ALL", BigDecimal.valueOf(200.00));
    }

    @Test
    public void calculatesAverageWithInvalidDateThrowsInvalidDataError() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withRecord("user1", "invalid-date", "transport", BigDecimal.valueOf(600.00))
                .when()
                .executeScript()
                .then()
                .containsError("❌ Обнаружена неверная дата. Завершаем.");
    }

    @Test
    public void calculatesAverageWithMissingDateThrowsInvalidDataError() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withRecord("user1", "", "transport", BigDecimal.valueOf(600.00))
                .when()
                .executeScript()
                .then()
                .containsError("❌ Обнаружена неверная дата. Завершаем.");
    }

    // Тест находит баг в скрипте
    @Test
    public void calculatesAverageWithMissingUserThrowsInvalidUserError() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withRecord("", "2025-01-01", "transport", BigDecimal.valueOf(600.00))
                .when()
                .executeScript()
                .then()
                .containsError("❌ Пользователь не указан");
    }

    // Тест находит баг в скрипте
    @Test
    public void сalculatesAverageWithInvalidCsvDelimiterThrowsInvalidDataError() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withInvalidDelimiter("user1", "2025-01-01", "food", BigDecimal.valueOf(600.00))
                .when()
                .executeScript()
                .then()
                .containsError("❌ Invalid data");
    }

    // Тест находит баг в скрипте
    @Test
    public void сalculatesAverageWithNegativeAmountThrowsInvalidDataError() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withRecord("user1", "2025-01-01", "food", BigDecimal.valueOf(-300.00))
                .when()
                .executeScript()
                .then()
                .containsError("❌ Invalid data: amount cannot be negative");
    }

    // Тест находит баг в скрипте
    @Test
    public void сalculatesAverageWithEmptyAmountThrowsInvalidDataError() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withRecord("user1", "2025-01-01", "food", "")
                .when()
                .executeScript()
                .then()
                .containsError("❌ Invalid data: amount cannot be empty");
    }

    // Тест находит баг в скрипте
    @Test
    public void calculatesAverageWithEmptyCsvFileThrowsInvalidDataError() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .when()
                .executeScript()
                .then()
                .containsError("❌ Invalid data: transactions.csv cannot be empty");
    }

    @Test
    public void calculatesAverageWithNonExistingInputFileThrowsFileNotFoundError() {
        String nonExistingFile = "file.txt";
        String expectedError = "Файл не найден: "
                + Path.of(nonExistingFile).toAbsolutePath().normalize();

        new TestScenario()
                .given()
                .generateRecords(1)
                .when()
                .executeScript(Path.of(nonExistingFile))
                .then()
                .containsError(expectedError);
    }

    @Test
    public void generatesAveragesCsvWithCorrectHeader() {
        new TestScenario()
                .given()
                .generateRecords(1)
                .when()
                .executeScript()
                .then()
                .hasCorrectHeader();
    }
}
