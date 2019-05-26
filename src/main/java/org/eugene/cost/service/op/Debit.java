package org.eugene.cost.service.op;

import org.eugene.cost.util.Calculate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Debit implements Operation {
    private String diff;
    private String description;
    private LocalDate date;

    public Debit(String diff, String description) {
        this.diff = diff;
        this.description = description;
        this.date = LocalDate.now();
    }

    public Debit(String diff, String description, LocalDate date) {
        this.diff = diff;
        this.description = description;
        this.date = date;
    }

    @Override
    public String execute(String balance) {
        return Calculate.minus(balance, diff);
    }

    @Override
    public String getSum() {
        return diff;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")) + " Списание " + diff + " Руб. "+description;
    }
}
