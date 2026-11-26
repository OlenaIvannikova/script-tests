import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class CheckActiveUsersTest {

    @Test
    public void bannedUserIsExcludedFromActiveUsers() {
        new TestScenario()
                .given()
                .generateUsersFile(0)
                .withUsers("Alice", "Bob")
                .generateLoginsFile(0)
                .withLoginRecord("Alice", LocalDate.now())
                .withLoginRecord("Bob", LocalDate.now())
                .withBannedUsers("Alice")
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsUser("Bob")
                .doesNotContainUser("Alice");
    }

    @Test
    public void userIsincludedInActiveUsersWhenUserIsNotBanned() {
        new TestScenario()
                .given()
                .generateUsersFile(0)
                .withUsers("Alice")
                .generateLoginsFile(0)
                .withLoginRecord("Alice", LocalDate.now())
                .withBannedUsers("Bob")
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsUser("Alice");
    }

    @Test
    public void userIsExcludedFromActiveUsersWithLastLoginOlderThan30Days() {
        new TestScenario()
                .given()
                .generateUsersFile(0)
                .withUsers("Alice")
                .generateLoginsFile(0)
                .withLoginRecord("Alice", LocalDate.now().minusDays(31))
                .withBannedUsers()
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .doesNotContainUser("Alice");
    }

    @Test
    public void userIsincludedInActiveUsersWhenLastLoginIsWithin30Days() {
        new TestScenario()
                .given()
                .generateUsersFile(0)
                .withUsers("Alice")
                .generateLoginsFile(0)
                .withLoginRecord("Alice", LocalDate.now().minusDays(10))
                .withBannedUsers("Bob")
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsUser("Alice");
    }

    @Test
    public void userIsincludedInActiveUsersWhenLastLoginIsExactly30DaysAgo() {
        new TestScenario()
                .given()
                .generateUsersFile(0)
                .withUsers("Alice")
                .generateLoginsFile(0)
                .withLoginRecord("Alice", LocalDate.now().minusDays(30))
                .withBannedUsers("Bob")
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .containsUser("Alice");
    }
}