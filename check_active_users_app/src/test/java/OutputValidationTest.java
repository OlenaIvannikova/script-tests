import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class OutputValidationTest {

    @Test
    public void createsActiveUsersFileWithCorrectHeader() {
        new TestScenario()
                .given()
                .generateUsersFile(0)
                .withUsers("Alice", "Bob")
                .generateLoginsFile(0)
                .withLoginRecord("Alice", LocalDate.now())
                .withLoginRecord("Bob", LocalDate.now())
                .withBannedUsers()
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .hasCorrectHeader();
    }

    @Test
    public void createsActiveUsersFileWithHeaderOnlyWhenNoUsersMeetCriteria() {
        new TestScenario()
                .given()
                .generateUsersFile(0)
                .withUsers("Alice")
                .generateLoginsFile(0)
                .withBannedUsers("Alice")
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsOnlyHeader();
    }
}