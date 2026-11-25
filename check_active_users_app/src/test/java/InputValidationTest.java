import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class InputValidationTest {
    @Test
    public void returnsOnlyHeaderWhenUserListIsEmpty() {
        new TestScenario()
                .given()
                .generateUsersFile(0)
                .generateLoginsFile(1)
                .withBannedUsers("Alice")
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsOnlyHeader();
    }

    // Тест находит баг в скрипте
    @Test
    public void returnsInvalidDataErrorWhenCsvDelimiterIsIncorrect() {
        new TestScenario()
                .given()
                .generateUsersFile(1)
                .generateLoginsFile(0)
                .withInvalidCsvDelimiter("Alice", LocalDate.now())
                .withBannedUsers("Alice")
                .when()
                .executeScript()
                .then()
                .exitCode(1)
                .containsError("Invalid data");
    }

    // Тест находит баг в скрипте
    @Test
    public void returnsInvalidDataErrorWhenLoginDateIsInvalidInCsvFile() {
        new TestScenario()
                .given()
                .generateUsersFile(1)
                .generateLoginsFile(0)
                .withLoginRecord("Alice", "invalid-date")
                .withBannedUsers()
                .when()
                .executeScript()
                .then()
                .exitCode(1)
                .containsError("Обнаружена неверная дата");
    }

    // Тест находит баг в скрипте
    @Test
    public void returnsInvalidDataErrorWhenLoginDateIsMissingFromCsvFile() {
        new TestScenario()
                .given()
                .generateUsersFile(1)
                .generateLoginsFile(0)
                .withLoginRecord("Alice", "")
                .withBannedUsers()
                .when()
                .executeScript()
                .then()
                .exitCode(1)
                .containsError("Обнаружена неверная дата");
    }

    // Тест находит баг в скрипте
    @Test
    public void returnsInvalidDataErrorWhenLoginIsMissingFromCsvFile() {
        new TestScenario()
                .given()
                .generateUsersFile(1)
                .generateLoginsFile(0)
                .withLoginRecord("", LocalDate.now())
                .withBannedUsers()
                .when()
                .executeScript()
                .then()
                .exitCode(1)
                .containsError("Логин отсутствует");
    }

    // Тест находит баг в скрипте
    @Test
    public void returnsInvalidDataErrorWhenBannedJsonIsInvalid() {
        new TestScenario()
                .given()
                .generateUsersFile(1)
                .generateLoginsFile(0)
                .withInvalidBannedJson()
                .when()
                .executeScript()
                .then()
                .exitCode(1)
                .containsError("Обнаружена неверная JSON");
    }
}