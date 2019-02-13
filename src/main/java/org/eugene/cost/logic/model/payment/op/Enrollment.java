package org.eugene.cost.logic.model.payment.op;

import org.eugene.cost.logic.util.Calculate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Enrollment implements Operation {
    private String diff;
    private String description;
    private LocalDate date;

    public Enrollment(String diff, String description) {
        this.diff = diff;
        this.description = description;
        this.date = LocalDate.now();
    }

    public Enrollment(String diff, String description, LocalDate date) {
        this.diff = diff;
        this.description = description;
        this.date = date;
    }

    @Override
    public String execute(String balance) {
        return Calculate.plus(balance, diff);
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
        return date.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")) + " Зачисление " + diff + " Руб. "+description;
    }
}
