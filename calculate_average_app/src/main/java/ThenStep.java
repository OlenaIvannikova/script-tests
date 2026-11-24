import data.ScriptResult;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThenStep {

    private final TestScenario scenario;

    ThenStep(TestScenario scenario) {
        this.scenario = scenario;
    }

    public ThenStep containsRecord(String user, String month, BigDecimal averageAmount) {
        scenario.resultAnalyser.assertRecord(user, month, averageAmount);
        return this;
    }

    public ThenStep containsError(String error) {
        scenario.resultAnalyser.assertError(scenario.scriptResult, error);
        return this;
    }

    public ThenStep hasCorrectHeader() {
        scenario.resultAnalyser.assertCorrectHeader();
        return this;
    }

    public ThenStep exitCode(int expectedExitCode) {
        assertEquals(expectedExitCode, scenario.scriptResult.getExitCode(),
                "Unexpected script exit code: " + scenario.scriptResult.getOutput()
        );
        return this;
    }

    public ThenStep executionTimeLessThanOrEqualTo(long expectedSeconds) {
        long actualTimeMillis = scenario.scriptResult.getExecutionTimeMillis();
        long expectedTimeMillis = expectedSeconds * 1000;

        assertTrue(actualTimeMillis <= expectedTimeMillis, "Expected execution time to be less than or equal to "
                + expectedSeconds + " seconds, but was " + actualTimeMillis + " ms");
        return this;
    }

    public ThenStep allRunsHaveExitCode(int expectedExitCode) {
        for (int i = 0; i < scenario.scriptResults.size(); i++) {
            ScriptResult result = scenario.scriptResults.get(i);

            assertEquals(expectedExitCode, result.getExitCode(), "Unexpected exit code on run " + (i + 1));
        }
        return this;
    }

    public ThenStep allRunsExecutionTimeLessThanOrEqualTo(long expectedSeconds) {
        long expectedTimeMillis = expectedSeconds * 1000;

        for (int i = 0; i < scenario.scriptResults.size(); i++) {
            ScriptResult result = scenario.scriptResults.get(i);

            assertTrue(result.getExecutionTimeMillis() <= expectedTimeMillis,
                    "Execution time on run " + (i + 1) + " exceeded " + expectedSeconds + " seconds. Actual: "
                            + result.getExecutionTimeMillis() + " ms");
        }
        return this;
    }
}