package org.eugene.cost.service;

import org.eugene.cost.data.Operation;
import org.eugene.cost.data.OperationType;
import org.eugene.cost.data.Payment;

import java.time.LocalDate;

public interface IOperationService {
    Operation create(Payment payment, String transactionAmount, String description,
                     OperationType operationType, LocalDate dateOfOperation);

    Operation create(Payment payment, String transactionAmount, String description,
                     OperationType operationType);
}
