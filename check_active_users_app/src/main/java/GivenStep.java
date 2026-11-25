import java.time.LocalDate;

public class GivenStep {

    private final TestScenario scenario;

    GivenStep(TestScenario scenario) {
        this.scenario = scenario;
    }

    // создание users.txt
    public GivenStep generateUsersFile(int num) {
        scenario.dataGenerator.createUsersFile(num);
        return this;
    }

    public GivenStep withUsers(String... usernames) {
        scenario.dataGenerator.withUsers(usernames);
        return this;
    }

    // создание logins.csv
    public GivenStep generateLoginsFile(int num) {
        scenario.dataGenerator.createLoginsFile(num);
        return this;
    }

    public GivenStep withLoginRecord(String login, LocalDate lastLogin) {
        scenario.dataGenerator.withLoginRecord(login, lastLogin);
        return this;
    }

    public GivenStep withLoginRecord(String login, String lastLogin) {
        scenario.dataGenerator.withLoginRecord(login, lastLogin);
        return this;
    }

    public GivenStep withInvalidCsvDelimiter(String login, LocalDate lastLogin) {
        scenario.dataGenerator.withInvalidDelimiter(login, lastLogin);
        return this;
    }

    // создание banned.json
    public GivenStep withBannedUsers(String... usernames) {
        scenario.dataGenerator.createBannedFile(usernames);
        return this;
    }

    public GivenStep withInvalidBannedJson() {
        scenario.dataGenerator.createInvalidBannedFile();
        return this;
    }

    public WhenStep when() {
        System.out.println("Executing actions..");
        return new WhenStep(scenario);
    }
}