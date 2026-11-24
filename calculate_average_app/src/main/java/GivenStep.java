import java.math.BigDecimal;

public class GivenStep {
    private final TestScenario scenario;

   GivenStep(TestScenario scenario) {
        this.scenario = scenario;
    }

    public GivenStep generateRecords(int num) {
        scenario.dataGenerator.generateRecords(num);
        return this;
    }

    public GivenStep withRecord(String user, String date, String category, BigDecimal amount) {
        scenario.dataGenerator.withRecord(user, date, category, amount);
        return this;
    }

    public GivenStep withRecord(String user, String date, String category, String amount) {
        scenario.dataGenerator.withRecord(user, date, category, amount);
        return this;
    }

    public GivenStep withInvalidDelimiter(String user, String date, String category, BigDecimal amount) {
        scenario.dataGenerator.withInvalidDelimiter(user, date, category, amount);
        return this;
    }

    public WhenStep when() {
        System.out.println("Executing actions..");
        return new WhenStep(scenario);
    }
}
