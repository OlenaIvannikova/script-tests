package data;

import java.math.BigDecimal;

public class AverageRecord {
    String user;
    String month;
    BigDecimal averageAmount;

    public AverageRecord(String user, String month, BigDecimal averageAmount) {
        this.user = user;
        this.month = month;
        this.averageAmount = averageAmount;
    }

    public String getUser() {
        return user;
    }

    public String getMonth() {
        return month;
    }

    public BigDecimal getAverageAmount() {
        return averageAmount;
    }
}
