import data.LoginNameMap;

import java.util.Map;

public class GivenStep {
    private final TestScenario scenario;

    GivenStep(TestScenario scenario) {
        this.scenario = scenario;
    }

    // создание users.txt
    public GivenStep generateUsersTxtFile(int num) {
        scenario.dataGenerator.createUsersTxtFile(num);
        return this;
    }

    public GivenStep withUsers(String... usernames) {
        scenario.dataGenerator.withUsers(usernames);
        return this;
    }

    // создание users.json: логин → имя
    public GivenStep withUsersJson(LoginNameMap... users) {
        scenario.dataGenerator.createUsersJsonFile(users);
        return this;
    }

    public GivenStep withInvalidUsersJson() {
        scenario.dataGenerator.createInvalidUsersJsonFile();
        return this;
    }

    // создание users.csv: логин, email
    public GivenStep generateUsersCsvFile(int num) {
        scenario.dataGenerator.createUsersCsvFile(num);
        return this;
    }

    public GivenStep withEmailRecord(String login, String email) {
        scenario.dataGenerator.withEmailRecord(login, email);
        return this;
    }

    public GivenStep withEmailRecords(Map<String, String> emailRecords) {
        scenario.dataGenerator.withEmailRecords(emailRecords);
        return this;
    }

    public GivenStep withInvalidCsvDelimiter(String login, String email) {
        scenario.dataGenerator.withInvalidDelimiter(login, email);
        return this;
    }

    public WhenStep when() {
        System.out.println("Executing actions..");
        return new WhenStep(scenario);
    }
}