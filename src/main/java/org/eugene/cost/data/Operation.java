package org.eugene.cost.data;

import java.io.Serializable;
import java.time.LocalDate;

public class Operation implements Serializable {
    private static final long serialVersionUID = 159101818276084917L;

    private String transactionAmount;
    private String description;
    private OperationType type;
    private LocalDate date;

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
