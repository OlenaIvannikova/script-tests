import org.junit.jupiter.api.Test;

public class CalculateAverageNonFunctionalTest {

    @Test
    public void processes10000TransactionsWithinExpectedTime() {
        new TestScenario()
                .given()
                .generateRecords(10000)
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .executionTimeLessThanOrEqualTo(1);
    }

    @Test
    public void processes100000TransactionsWithinExpectedTime () {
        new TestScenario()
                .given()
                .generateRecords(100000)
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .executionTimeLessThanOrEqualTo(5);
    }

    @Test
    public void processes1000000TransactionsWithinExpectedTime() {
        new TestScenario()
                .given()
                .generateRecords(1000000)
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .executionTimeLessThanOrEqualTo(20);
    }

    @Test
    public void processes10001TransactionsWithinExpectedTime() {
        new TestScenario()
                .given()
                .generateRecords(10001)
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .executionTimeLessThanOrEqualTo(5);
    }

    @Test
    public void processes100001TransactionsWithinExpectedTime() {
        new TestScenario()
                .given()
                .generateRecords(100001)
                .when()
                .executeScript()
                .then()
                .exitCode(0)
                .executionTimeLessThanOrEqualTo(20);
    }

    @Test
    public void maintainsStablePerformanceAcrossRepeatedRuns(){
        new TestScenario()
                .given()
                .generateRecords(100000)
                .when()
                .executeScriptRepeatedly(5)
                .then()
                .allRunsHaveExitCode(0)
                .allRunsExecutionTimeLessThanOrEqualTo(5);
    }
}
