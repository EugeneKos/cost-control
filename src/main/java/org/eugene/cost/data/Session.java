package org.eugene.cost.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Session implements Serializable {
    private final String limit;
    private String balance;
    private final LocalDate beginDate;
    private final LocalDate finalDate;
    private final List<Day> days;

    public Session(String limit, LocalDate beginDate, LocalDate finalDate, List<Day> days) {
        this.limit = limit;
        this.balance = limit;
        this.beginDate = beginDate;
        this.finalDate = finalDate;
        this.days = days;
    }

    public boolean isActiveSession(){
        return finalDate.isAfter(LocalDate.now());
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public String getLimit() {
        return limit;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalance() {
        return balance;
    }

    public List<Day> getDays() {
        return days;
    }
}
