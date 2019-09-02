package org.eugene.cost.data;

import java.time.LocalDate;
import java.util.Objects;

public class Cash extends Payment {
    private static final long serialVersionUID = -8766841779999878675L;

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
