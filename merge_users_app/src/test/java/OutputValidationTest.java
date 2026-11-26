import data.LoginNameMap;
import org.junit.jupiter.api.Test;

public class OutputValidationTest {

    @Test
    public void shouldCreateOutputFileWithCorrectHeader() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .withUsers("alice")
                .generateUsersCsvFile(0)
                .withEmailRecord("alice", "alice@example.com")
                .withUsersJson(new LoginNameMap("alice", "Alice Smith"))
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .hasCorrectHeader();
    }

    @Test
    public void shouldCreateOutputFileWithHeaderOnlyWhenNoUsersMatch() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .withUsers("bob")
                .generateUsersCsvFile(0)
                .withEmailRecord("alice", "alice@example.com")
                .withUsersJson(new LoginNameMap("alice", "Alice Smith"))
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsOnlyHeader();
    }
}