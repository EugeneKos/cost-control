package org.eugene.cost.data.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

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

        return Objects.equals(description, cash.description);
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
