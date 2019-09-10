package org.eugene.cost.service;

import org.eugene.cost.data.Operation;
import org.eugene.cost.data.OperationType;
import org.eugene.cost.data.Payment;
import org.eugene.cost.data.PaymentOperation;
import org.eugene.cost.exeption.NotEnoughMoneyException;

import java.time.LocalDate;
import java.util.List;

public interface IOperationService {
    void create(PaymentOperation paymentOperation, String transactionAmount, String description,
                     OperationType operationType, LocalDate dateOfOperation) throws NotEnoughMoneyException;

    void create(PaymentOperation paymentOperation, String transactionAmount, String description,
                     OperationType operationType) throws NotEnoughMoneyException;

    List<Operation> getOperationsByPayment(Payment payment, boolean isIncrease);
}
