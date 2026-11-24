import data.ScriptResult;

import java.nio.file.Path;

public class WhenStep {

    private final TestScenario scenario;

    WhenStep(TestScenario scenario) {
        this.scenario = scenario;
    }

    public WhenStep executeScript() {
        scenario.scriptResult = scenario.scriptRunner.executeScript();
        return this;
    }

    public WhenStep executeScript(Path filePath) {
        scenario.scriptResult = scenario.scriptRunner.executeScript(filePath);
        return this;
    }

    public WhenStep executeScriptRepeatedly(int numberOfRuns) {
        if (numberOfRuns <= 0) {
            throw new IllegalArgumentException(
                    "Number of runs must be greater than zero"
            );
        }

        scenario.scriptResults.clear();

        for (int i = 0; i < numberOfRuns; i++) {
            ScriptResult result = scenario.scriptRunner.executeScript();
            scenario.scriptResults.add(result);
        }
        return this;
    }

    public ThenStep then() {
        System.out.println("Check results..");
        return new ThenStep(scenario);
    }
}