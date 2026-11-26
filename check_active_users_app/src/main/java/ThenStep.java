import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThenStep {
    private final TestScenario scenario;

    ThenStep(TestScenario scenario) {
        this.scenario = scenario;
    }

    public ThenStep exitCode(int expectedExitCode) {
        assertEquals(expectedExitCode, scenario.scriptResult.getExitCode(),
                "Unexpected script exit code: " + scenario.scriptResult.getOutput()
        );
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

    public ThenStep containsOnlyHeader() {
        scenario.resultAnalyser.assertContainsOnlyHeader();
        return this;
    }

    public ThenStep containsUser(String login) {
        scenario.resultAnalyser.assertUser(login);
        return this;
    }

    public ThenStep doesNotContainUser(String login) {
        scenario.resultAnalyser.assertNotContainUser(login);
        return this;
    }
}