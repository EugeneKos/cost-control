package org.eugene.cost.logic.model.card.op;

import org.eugene.cost.logic.util.Calculate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Debit implements Operation {
    private String diff;
    private String description;
    private LocalDate date;

    public Debit(String diff, String description) {
        this.diff = diff;
        this.description = description;
        date = LocalDate.now();
    }

    @Override
    public String execute(String balance) {
        return Calculate.minus(balance, diff);
    }

    @Override
    public String getDescription() {
        return date.format(DateTimeFormatter.ofPattern("dd/MMM/yyyy")) + " Списание " + diff + " Руб. "+description;
    }

    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ofPattern("dd/MMM/yyyy")) + " Списание " + diff + " Руб. "+description;
    }
}
