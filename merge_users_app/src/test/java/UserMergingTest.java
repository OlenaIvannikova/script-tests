import data.LoginNameMap;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class UserMergingTest {

    @Test
    public void shouldIncludeUserWhenCompleteDataExistsInAllFiles() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .withUsers("alice")
                .generateUsersCsvFile(0)
                .withEmailRecord("alice", "alice@example.com")
                .withUsersJson(new LoginNameMap("alice", "Alice Smith"), new LoginNameMap("bob", "Bob Johnson"))
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsRecordCount(1)
                .containsUser("alice");
    }

    @Test
    public void shouldIncludeAllUsersWhenCompleteDataExistsInAllFiles() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .withUsers("alice", "bob", "carol")
                .generateUsersCsvFile(0)
                .withEmailRecords(Map.of(
                        "alice", "alice@example.com",
                        "bob", "bob@example.com",
                        "carol", "carol@example.com"
                ))
                .withUsersJson(
                        new LoginNameMap("alice", "Alice Smith"),
                        new LoginNameMap("bob", "Bob Johnson"),
                        new LoginNameMap("carol", "Carol Lee"))
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsRecordCount(3)
                .containsUsers("alice", "bob", "carol");
    }

    @Test
    public void shouldSkipUserWhenLoginIsMissingInJsonFile() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .withUsers("alice")
                .generateUsersCsvFile(0)
                .withEmailRecord("alice", "alice@example.com")
                .withUsersJson(new LoginNameMap("bob", "Bob Johnson"))
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsRecordCount(0)
                .doesNotContainUser("alice");
    }

    @Test
    public void shouldSkipUserWhenLoginIsMissingInCsvFile() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .withUsers("alice")
                .generateUsersCsvFile(0)
                .withEmailRecord("bob", "bob@example.com")
                .withUsersJson(new LoginNameMap("alice", "Alice Smith"))
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsRecordCount(0)
                .doesNotContainUser("alice");
    }

    @Test
    public void shouldSkipUserWhenLoginExistsOnlyInUsersFile() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .withUsers("alice")
                .generateUsersCsvFile(0)
                .withUsersJson(new LoginNameMap("", ""))
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsRecordCount(0)
                .doesNotContainUser("alice");
    }

    @Test
    public void shouldSkipUserWhenLoginExistsOnlyInCsvAndJsonFiles() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .generateUsersCsvFile(0)
                .withEmailRecord("bob", "bob@example.com")
                .withUsersJson(new LoginNameMap("bob", "Bob Johnson"))
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsRecordCount(0)
                .doesNotContainUser("bob");
    }

    @Test
    public void shouldIncludeUserOnlyOnceWhenLoginIsDuplicated() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .withUsers("alice")
                .withUsers("alice")
                .generateUsersCsvFile(0)
                .withEmailRecord("alice", "alice@example.com")
                .withUsersJson(new LoginNameMap("alice", "Alice Smith"), new LoginNameMap("bob", "Bob Johnson"))
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsUserExactlyOnce("alice");
    }
}