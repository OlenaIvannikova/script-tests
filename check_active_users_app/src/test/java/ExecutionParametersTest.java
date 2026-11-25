import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;

import static helpers.DataGenerator.*;

public class ExecutionParametersTest {

    @Test
    public void failsWhenScriptIsExecutedWithoutArguments() {
        new TestScenario()
                .given()
                .generateUsersFile(0)
                .withUsers("Alice")
                .generateLoginsFile(0)
                .withLoginRecord("Alice", LocalDate.now())
                .withBannedUsers("Bob")
                .when()
                .executeScript(new Path[0])
                .then()
                .exitCode(1)
                .containsError("❌ Один из входных файлов не найден.");
    }

    @Test
    public void failsWhenScriptIsExecutedWithTwoArguments() {
        new TestScenario()
                .given()
                .generateUsersFile(0)
                .withUsers("Alice")
                .generateLoginsFile(0)
                .withLoginRecord("Alice", LocalDate.now())
                .withBannedUsers()
                .when()
                .executeScript(USERS_FILE, LOGINS_FILE)
                .then()
                .exitCode(1)
                .containsError("❌ Один из входных файлов не найден.");
    }

    @Test
    public void failsWhenInputFilePathDoesNotExist() {
        String nonExistingFile = "file.txt";
        new TestScenario()
                .given()
                .generateUsersFile(0)
                .withUsers("Alice")
                .generateLoginsFile(0)
                .withLoginRecord("Alice", LocalDate.now())
                .withBannedUsers("Bob")
                .when()
                .executeScript(Path.of(nonExistingFile), LOGINS_FILE, BANNED_FILE)
                .then()
                .exitCode(-1)
                .containsError("Input file not found: file.txt");
    }
}