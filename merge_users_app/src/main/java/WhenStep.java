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

    public WhenStep executeScript(Path... inputFiles) {
        scenario.scriptResult = scenario.scriptRunner.executeScript(inputFiles);
        return this;
    }

    public ThenStep then() {
        System.out.println("Check results..");
        return new ThenStep(scenario);
    }
}