import data.LoginNameMap;
import org.junit.jupiter.api.Test;

public class InputValidationTest {

    @Test
    public void shouldCreateOutputWithHeaderOnlyWhenUsersFileIsEmpty() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .generateUsersCsvFile(0)
                .withEmailRecord("alice", "alice@example.com")
                .withUsersJson(new LoginNameMap("alice", "Alice Smith"))
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsOnlyHeader();
    }

    // Тест находит баг в скрипте: exitCode должен быть 1
    @Test
    public void shouldRejectCsvFileWithInvalidDelimiter() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .withUsers("alice")
                .generateUsersCsvFile(0)
                .withInvalidCsvDelimiter("alice", "alice@example.com")
                .withUsersJson(new LoginNameMap("alice", "Alice Smith"))
                .when()
                .executeScript()
                .then()
                .exitCode(1)
                .containsError("⚠ Пропущен пользователь");
    }

    // Тест находит баг в скрипте: exitCode должен быть 1
    @Test
    public void shouldRejectCsvRecordWithMissingEmail() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .withUsers("alice")
                .generateUsersCsvFile(0)
                .withEmailRecord("alice", "")
                .withUsersJson(new LoginNameMap("alice", "Alice Smith"))
                .when()
                .executeScript()
                .then()
                .exitCode(1)
                .containsError("⚠ Пропущен пользователь");
    }

    // Тест находит баг в скрипте: exitCode должен быть 1
    @Test
    public void shouldRejectCsvRecordWithMissingLogin() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .withUsers("alice")
                .generateUsersCsvFile(0)
                .withEmailRecord("", "alice@example.com")
                .withUsersJson(new LoginNameMap("alice", "Alice Smith"))
                .when()
                .executeScript()
                .then()
                .exitCode(1)
                .containsError("⚠ Пропущен пользователь");
    }

    // Тест находит баг в скрипте: exitCode должен быть 1
    @Test
    public void shouldRejectInvalidJsonFormat() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .withUsers("alice")
                .generateUsersCsvFile(0)
                .withEmailRecord("alice", "alice@example.com")
                .withInvalidUsersJson()
                .when()
                .executeScript()
                .then()
                .exitCode(1)
                .containsError("⚠ Пропущен пользователь");
    }
}