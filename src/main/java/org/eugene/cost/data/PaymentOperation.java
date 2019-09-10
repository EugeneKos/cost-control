package org.eugene.cost.data;

public class PaymentOperation {
    private Payment first;
    private Payment second;

    public PaymentOperation(Payment first, Payment second) {
        this.first = first;
        this.second = second;
    }

    public Payment getFirst() {
        return first;
    }

    public Payment getSecond() {
        return second;
    }
}
