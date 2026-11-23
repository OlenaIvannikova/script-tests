import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Path;

public class CalculateAverageTest {

    @Test
    public void calculatingAverageWithTwoUsersAndOneRecordWorksCorrectly() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withRecord("user1", "2025-01-01", "transport", BigDecimal.valueOf(800.00))
                .withRecord("user2", "2025-01-02", "food", BigDecimal.valueOf(100.00))
                .when().executeScript()
                .then()
                .containsRecord("user1", "ALL", BigDecimal.valueOf(800.00))
                .containsRecord("user2", "ALL", BigDecimal.valueOf(100.00));
    }

    @Test
    public void calculatingAverageWithInvalidDataLeadsToInvalidDataError() {
        new TestScenario()
                .given()
                .generateRecords(0)
                .withRecord("user1", "invalid-date", "transport", BigDecimal.valueOf(800.00))
                .when()
                .executeScript()
                .then()
                .containsError("❌ Обнаружена неверная дата. Завершаем.");
    }

    @Test
    public void calculatingAverageWithNonExistingInputFileLeadsToFileNotFoundError() {
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
}
