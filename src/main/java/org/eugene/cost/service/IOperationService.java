package org.eugene.cost.service;

import org.eugene.cost.data.Operation;
import org.eugene.cost.data.OperationFilter;
import org.eugene.cost.data.OperationType;
import org.eugene.cost.data.Payment;
import org.eugene.cost.data.PaymentOperation;
import org.eugene.cost.exeption.NotEnoughMoneyException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IOperationService {
    void create(PaymentOperation paymentOperation, String transactionAmount, String description,
                     OperationType operationType, LocalDate dateOfOperation) throws NotEnoughMoneyException;

    void create(PaymentOperation paymentOperation, String transactionAmount, String description,
                     OperationType operationType) throws NotEnoughMoneyException;

    List<Operation> getOperationsByPayment(Payment payment, OperationFilter filter);

    Map<LocalDate, Double> getTransactionsAmountsByTypeAndDates(Payment payment, OperationType type,
                                                                LocalDate beginDate, LocalDate finalDate);

    Map<LocalDate, Double> getBalancesByDates(Payment payment, LocalDate beginDate, LocalDate finalDate);
}
