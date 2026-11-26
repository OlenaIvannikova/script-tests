package data;

import java.time.LocalDate;

public class LoginRecord {
    String login;
    LocalDate lastLogin;

    public LoginRecord(String login, LocalDate lastLogin) {
        this.login = login;
        this.lastLogin = lastLogin;
    }

    public String getLogin() {
        return login;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }
}