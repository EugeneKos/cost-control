package org.eugene.cost.logic.model.card.bank;

import org.eugene.cost.logic.model.card.op.Operation;

import java.util.LinkedList;
import java.util.List;

public abstract class Bank {
    private String balance;

    private List<Operation> operationHistory = new LinkedList<>();

    Bank(String balance) {
        this.balance = balance;
    }

    public String getBalance() {
        return balance;
    }

    public List<Operation> getOperationHistory() {
        return operationHistory;
    }

    public void executeOperation(Operation operation){
        balance = operation.execute(balance);
        operationHistory.add(operation);
    }
}
