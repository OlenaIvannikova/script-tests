package data;

public class UserRecord {
    String login;
    String name;
    String email;

    public UserRecord(String login, String name, String email) {
        this.login = login;
        this.name = name;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }
}
