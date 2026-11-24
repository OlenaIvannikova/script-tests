import data.ScriptResult;
import helpers.DataGenerator;
import helpers.ResultAnalyser;
import helpers.ScriptRunner;

import java.util.ArrayList;
import java.util.List;

public class TestScenario {

    public final DataGenerator dataGenerator;
    public final ScriptRunner scriptRunner;
    public final ResultAnalyser resultAnalyser;

    public ScriptResult scriptResult;
    public List<ScriptResult> scriptResults = new ArrayList<>();

    public TestScenario() {
        this.dataGenerator = new DataGenerator();
        this.scriptRunner = new ScriptRunner();
        this.resultAnalyser = new ResultAnalyser();
    }

    public GivenStep given() {
        System.out.println("Initializing data..");
        return new GivenStep(this);
    }
}
