import data.ScriptResult;
import helpers.DataGenerator;
import helpers.ResultAnalyser;
import helpers.ScriptRunner;

import java.math.BigDecimal;
import java.nio.file.Path;

public class TestScenario {

    private DataGenerator dataGenerator;
    private ScriptRunner scriptRunner;
    private ResultAnalyser resultAnalyser;

    private ScriptResult scriptResult;

    public TestScenario() {
        this.dataGenerator = new DataGenerator();
        this.scriptRunner = new ScriptRunner();
        this.resultAnalyser = new ResultAnalyser();
    }

    public TestScenario given() {
        System.out.println("Initializing data..");
        return this;
    }

    public TestScenario generateRecords(int num) {
        dataGenerator.generateRecords(num);
        return this;
    }

    public TestScenario withRecord(String user, String date, String category, BigDecimal amount) {
        dataGenerator.withRecord(user, date, category, amount);
        return this;
    }

    public TestScenario when() {
        System.out.println("Executing actions..");
        return this;
    }

    public TestScenario executeScript() {
        scriptResult = scriptRunner.executeScript();
        return this;
    }

    public TestScenario executeScript(Path filePath) {
        scriptResult = scriptRunner.executeScript(filePath);
        return this;
    }

    public TestScenario then() {
        System.out.println("Check results..");
        return this;
    }

    public TestScenario containsRecord(String user, String month, BigDecimal averageAmount) {
        resultAnalyser.assertRecord(user, month, averageAmount);
        return this;
    }

    public TestScenario containsError(String error) {
        resultAnalyser.assertError(scriptResult, error);
        return this;
    }
}
