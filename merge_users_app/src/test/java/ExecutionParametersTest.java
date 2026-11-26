import data.LoginNameMap;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static helpers.ScriptRunner.*;

public class ExecutionParametersTest {

    @Test
    public void shouldFailWhenNoArgumentsAreProvided() {
        {
            new TestScenario()
                    .given()
                    .generateUsersTxtFile(0)
                    .withUsers("alice")
                    .generateUsersCsvFile(0)
                    .withEmailRecord("alice", "alice@example.com")
                    .withUsersJson(new LoginNameMap("alice", "Alice Smith"))
                    .when()
                    .executeScript(new Path[0])
                    .then()
                    .exitCode(1)
                    .containsError("Один из входных файлов не найден");
        }
    }

    @Test
    public void shouldFailWhenRequiredArgumentIsMissing() {
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .withUsers("alice")
                .generateUsersCsvFile(0)
                .withEmailRecord("alice", "alice@example.com")
                .withUsersJson(new LoginNameMap("alice", "Alice Smith"))
                .when()
                .executeScript(USERS_TXT_FILE, USERS_CSV_FILE)
                .then()
                .exitCode(1)
                .containsError("Один из входных файлов не найден");
    }

    @Test
    public void shouldFailWhenInputFileDoesNotExist() {
        String nonExistingFile = "file.txt";
        new TestScenario()
                .given()
                .generateUsersTxtFile(0)
                .withUsers("alice")
                .generateUsersCsvFile(0)
                .withEmailRecord("alice", "alice@example.com")
                .withUsersJson(new LoginNameMap("alice", "Alice Smith"))
                .when()
                .executeScript(Path.of(nonExistingFile), USERS_CSV_FILE, USERS_JSON_FILE)
                .then()
                .exitCode(-1)
                .containsError("Input file not found: file.txt");
    }
}