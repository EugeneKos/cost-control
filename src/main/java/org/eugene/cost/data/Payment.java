package org.eugene.cost.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public abstract class Payment implements Serializable {
    private static final long serialVersionUID = -4401576950880140962L;

    private String balance;
    private final LocalDate date;

    private List<Operation> operations = new LinkedList<>();

    public Payment(String balance) {
        this.balance = balance;
        this.date = LocalDate.now();
    }

    public Payment(String balance, LocalDate date) {
        this.balance = balance;
        this.date = date;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
}
