package org.eugene.cost.data.model;

import org.eugene.cost.service.op.Operation;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public abstract class Bank implements Serializable {
    private String balance;
    private final LocalDate date;

    private List<Operation> operationHistory = new LinkedList<>();

    public Bank(String balance) {
        this.balance = balance;
        this.date = LocalDate.now();
    }

    public Bank(String balance, LocalDate date) {
        this.balance = balance;
        this.date = date;
    }

    public String getBalance() {
        return balance;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Operation> getOperationHistory() {
        return operationHistory;
    }

    public void executeOperation(Operation operation){
        balance = operation.execute(balance);
        operationHistory.add(operation);
    }
}
