package org.eugene.cost.data.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Card extends Bank implements Serializable {
    private final String number;

    public Card(String balance, String number) {
        super(balance);
        this.number = number;
    }

    public Card(String balance, String number, LocalDate date) {
        super(balance, date);
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return number != null ? number.equals(card.number) : card.number == null;
    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }

    @Override
    public String toString() {
        return number;
    }
}
