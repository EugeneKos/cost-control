package org.eugene.cost.service.impl;

import org.eugene.cost.data.Operation;
import org.eugene.cost.data.OperationType;
import org.eugene.cost.data.Payment;
import org.eugene.cost.service.IOperationService;
import org.eugene.cost.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OperationServiceImpl implements IOperationService {
    private IPaymentService paymentService;

    @Autowired
    public OperationServiceImpl(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public Operation create(Payment payment, String transactionAmount, String description,
                            OperationType operationType, LocalDate dateOfOperation) {

        return createOperation(payment, transactionAmount, description, operationType, dateOfOperation);
    }

    @Override
    public Operation create(Payment payment, String transactionAmount, String description,
                            OperationType operationType) {

        return createOperation(payment, transactionAmount, description, operationType, null);
    }

    private Operation createOperation(Payment payment, String transactionAmount, String description,
                                      OperationType operationType, LocalDate dateOfOperation){

        Operation operation = new Operation();
        operation.setTransactionAmount(transactionAmount);
        operation.setDescription(description);
        operation.setType(operationType);
        if(dateOfOperation == null){
            operation.setDate(LocalDate.now());
        } else {
            operation.setDate(dateOfOperation);
        }
        saveOperation(payment, operation);
        return operation;
    }

    private void saveOperation(Payment payment, Operation operation){
        List<Operation> operations = payment.getOperations();
        operations.add(operation);
        payment.setOperations(operations);
        paymentService.update(payment);
    }
}
