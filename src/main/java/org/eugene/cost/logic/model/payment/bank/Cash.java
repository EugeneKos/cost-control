package org.eugene.cost.logic.model.payment.bank;

import java.io.Serializable;
import java.time.LocalDate;

public class Cash extends Bank implements Serializable {
    private final String description;

    public Cash(String balance, String description) {
        super(balance);
        this.description = description;
    }

    public Cash(String balance, String description, LocalDate date) {
        super(balance, date);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cash cash = (Cash) o;

        return description != null ? description.equals(cash.description) : cash.description == null;
    }

    @Override
    public int hashCode() {
        return description != null ? description.hashCode() : 0;
    }

    @Override
    public String toString() {
        return description;
    }
}
