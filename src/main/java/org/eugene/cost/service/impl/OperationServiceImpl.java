package org.eugene.cost.service.impl;

import org.eugene.cost.data.Operation;
import org.eugene.cost.data.OperationType;
import org.eugene.cost.data.Payment;
import org.eugene.cost.data.PaymentOperation;
import org.eugene.cost.exeption.NotEnoughMoneyException;
import org.eugene.cost.service.IOperationService;
import org.eugene.cost.service.IPaymentService;
import org.eugene.cost.service.util.Calculate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationServiceImpl implements IOperationService {
    private IPaymentService paymentService;

    @Autowired
    public OperationServiceImpl(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void create(PaymentOperation paymentOperation, String transactionAmount, String description,
                            OperationType operationType, LocalDate dateOfOperation) throws NotEnoughMoneyException {

        createOperation(paymentOperation, transactionAmount, description, operationType, dateOfOperation);
    }

    @Override
    public void create(PaymentOperation paymentOperation, String transactionAmount, String description,
                            OperationType operationType) throws NotEnoughMoneyException {

        createOperation(paymentOperation, transactionAmount, description, operationType, null);
    }

    @Override
    public List<Operation> getOperationsByPayment(Payment payment, boolean isIncrease) {
        return payment.getOperations().stream()
                .sorted((op1, op2) -> compareOperations(op1, op2, isIncrease))
                .collect(Collectors.toList());
    }

    private void createOperation(PaymentOperation paymentOperation, String transactionAmount, String description,
                                      OperationType operationType, LocalDate dateOfOperation) throws NotEnoughMoneyException {

        Operation operation = new Operation();
        operation.setTransactionAmount(transactionAmount);
        operation.setDescription(description);
        operation.setType(operationType);
        if(dateOfOperation == null){
            operation.setDate(LocalDate.now());
        } else {
            operation.setDate(dateOfOperation);
        }
        changeBalanceAndSaveOperation(paymentOperation, operation);
    }

    private void changeBalanceAndSaveOperation(PaymentOperation paymentOperation, Operation operation) throws NotEnoughMoneyException {
        Payment payment = paymentOperation.getFirst();
        String transactionAmount = operation.getTransactionAmount();
        switch (operation.getType()){
            case ENROLLMENT:
                payment.setBalance(Calculate.plus(payment.getBalance(), transactionAmount));
                saveOperation(payment, operation);
                break;
            case DEBIT:
                payment.setBalance(debitBalance(payment, transactionAmount));
                saveOperation(payment, operation);
                break;
            case TRANSFER:
                transferOperation(paymentOperation, operation);
                break;
        }
    }

    private void transferOperation(PaymentOperation paymentOperation, Operation operation) throws NotEnoughMoneyException {
        Payment first = paymentOperation.getFirst();
        Payment second = paymentOperation.getSecond();

        String transactionAmount = operation.getTransactionAmount();

        first.setBalance(debitBalance(first, transactionAmount));
        second.setBalance(Calculate.plus(second.getBalance(), transactionAmount));

        Operation operationForFirstPayment = new Operation(operation);
        operationForFirstPayment.setDescription("Перевод средств --> " + second.getIdentify());

        Operation operationForSecondPayment = new Operation(operation);
        operationForSecondPayment.setDescription("Зачисление средств от: " + first.getIdentify());

        saveOperation(first, operationForFirstPayment);
        saveOperation(second, operationForSecondPayment);
    }

    private void saveOperation(Payment payment, Operation operation){
        List<Operation> operations = payment.getOperations();
        operations.add(operation);
        payment.setOperations(operations);
        paymentService.update(payment);
    }

    private String debitBalance(Payment payment, String transactionAmount) throws NotEnoughMoneyException {
        String debit = Calculate.minus(payment.getBalance(), transactionAmount);
        if(Integer.parseInt(debit) < 0){
            throw new NotEnoughMoneyException("Ошибка операции " + OperationType.DEBIT.toString()
                    + ": Недостаточно средсв на " + payment.getIdentify());
        }
        return debit;
    }

    private int compareOperations(Operation one, Operation two, boolean isIncrease){
        if(one.getDate().isBefore(two.getDate())){
            if(isIncrease){
                return -1;
            } else {
                return 1;
            }
        }
        if(isIncrease){
            return one.getDate().isEqual(two.getDate()) ? 0 : 1;
        } else {
            return one.getDate().isEqual(two.getDate()) ? 0 : -1;
        }
    }
}
