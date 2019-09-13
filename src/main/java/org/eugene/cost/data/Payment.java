package org.eugene.cost.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Payment implements Serializable {
    private static final long serialVersionUID = -4401576950880140962L;

    private String identify;
    private String balance;

    private PaymentType paymentType;

    private final LocalDate dateOfCreation;

    private List<Operation> operations = new LinkedList<>();

    public Payment(String identify, String balance, PaymentType paymentType) {
        this.identify = identify;
        this.balance = balance;
        this.paymentType = paymentType;
        this.dateOfCreation = LocalDate.now();
    }

    public Payment(String identify, String balance, PaymentType paymentType, LocalDate dateOfCreation) {
        this.identify = identify;
        this.balance = balance;
        this.paymentType = paymentType;
        this.dateOfCreation = dateOfCreation;
    }

    public String getIdentify() {
        return identify;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(identify, payment.identify);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identify);
    }

    @Override
    public String toString() {
        return identify;
    }
}
