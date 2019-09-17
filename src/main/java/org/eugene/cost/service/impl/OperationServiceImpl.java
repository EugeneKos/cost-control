package org.eugene.cost.service.impl;

import org.apache.log4j.Logger;

import org.eugene.cost.data.Operation;
import org.eugene.cost.data.OperationFilter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OperationServiceImpl implements IOperationService {
    private static Logger LOGGER = Logger.getLogger(OperationServiceImpl.class);

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
        if(Double.parseDouble(debit) < 0){
            throw new NotEnoughMoneyException("Ошибка операции " + OperationType.DEBIT.toString()
                    + ": Недостаточно средсв на " + payment.getIdentify());
        }
        return debit;
    }

    @Override
    public List<Operation> getOperationsByPayment(Payment payment, OperationFilter filter) {
        LocalDate beginDate = filter.getBeginOperationsDate() == null
                ? payment.getDateOfCreation()
                : filter.getBeginOperationsDate();

        LocalDate finalDate = filter.getFinalOperationsDate() == null
                ? LocalDate.now()
                : filter.getFinalOperationsDate();

        return payment.getOperations().stream()
                .filter(operation -> dateFilter(operation, beginDate, finalDate))
                .sorted((op1, op2) -> compareOperations(op1, op2, filter.isIncrease()))
                .collect(Collectors.toList());
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

    private boolean dateFilter(Operation operation, LocalDate beginDate, LocalDate finalDate){
        LocalDate operationDate = operation.getDate();
        boolean conditionByBeginDate = operationDate.isAfter(beginDate) || operationDate.isEqual(beginDate);
        boolean conditionByFinalDate = operationDate.isBefore(finalDate) || operationDate.isEqual(finalDate);
        return conditionByBeginDate && conditionByFinalDate;
    }

    @Override
    public Map<LocalDate, Double> getTransactionsAmountsByTypeAndDates(Payment payment, OperationType type,
                                                                       LocalDate beginDate, LocalDate finalDate) {

        LocalDate newBeginDate = beginDate == null || beginDate.isBefore(payment.getDateOfCreation())
                ? payment.getDateOfCreation()
                : beginDate;
        LocalDate newFinalDate = finalDate == null || finalDate.isAfter(LocalDate.now())
                ? LocalDate.now()
                : finalDate;

        Map<LocalDate, Double> transactionAmountsByTypeAndDates = new HashMap<>();

        List<Operation> operations = payment.getOperations().stream()
                .filter(operation -> {
                    String keyWordTransferOperation;
                    switch (type){
                        case DEBIT:
                            keyWordTransferOperation = "Перевод";
                            break;
                        case ENROLLMENT:
                            keyWordTransferOperation = "Зачисление";
                            break;
                            default:
                                LOGGER.error("Для данной операции поддерживаются только" +
                                        " два типа операции: " + OperationType.ENROLLMENT + ", " + OperationType.DEBIT);
                                throw new IllegalArgumentException("Для данной операции поддерживаются только" +
                                        " два типа операции: " + OperationType.ENROLLMENT + ", " + OperationType.DEBIT);
                    }
                    if(operation.getType() == type){
                        return true;
                    }
                    if(operation.getType() == OperationType.TRANSFER){
                        return operation.getDescription().contains(keyWordTransferOperation);
                    }
                    return false;
                }).collect(Collectors.toList());

        LocalDate currentDate = newBeginDate;

        while (currentDate.isBefore(newFinalDate) || currentDate.isEqual(newFinalDate)){
            List<Operation> operationsByCurrentDate = getOperationsByDate(operations, currentDate);

            operations.removeAll(operationsByCurrentDate);

            String sumTransactions = getSumTransactions(operationsByCurrentDate);

            transactionAmountsByTypeAndDates.put(currentDate, Double.valueOf(sumTransactions));

            currentDate = currentDate.plusDays(1);
        }

        return transactionAmountsByTypeAndDates;
    }

    private List<Operation> getOperationsByDate(List<Operation> operations, LocalDate date){
        return operations.stream()
                .filter(operation -> date.isEqual(operation.getDate()))
                .collect(Collectors.toList());
    }

    private String getSumTransactions(List<Operation> operations){
        String sum = "0";
        for (Operation operation : operations){
            sum = Calculate.plus(sum, operation.getTransactionAmount());
        }
        return sum;
    }

    @Override
    public Map<LocalDate, Double> getBalancesByDates(Payment payment, LocalDate beginDate, LocalDate finalDate) {
        beginDate = beginDate == null || beginDate.isBefore(payment.getDateOfCreation())
                ? payment.getDateOfCreation()
                : beginDate;
        finalDate = finalDate == null || finalDate.isAfter(LocalDate.now())
                ? LocalDate.now()
                : finalDate;

        Map<LocalDate, Double> balancesByDates = new HashMap<>();

        String paymentBalance = payment.getBalance();

        LocalDate currentDate = finalDate;
        while (currentDate.isAfter(beginDate) || currentDate.isEqual(beginDate)){
            paymentBalance = getBalanceWithoutOperations(payment, paymentBalance, currentDate);

            balancesByDates.put(currentDate, Double.valueOf(paymentBalance));

            currentDate = currentDate.minusDays(1);
        }

        return balancesByDates;
    }

    private String getBalanceWithoutOperations(Payment payment, String initBalance, LocalDate date){
        List<Operation> operations = payment.getOperations().stream()
                .filter(operation -> operation.getDate().isEqual(date))
                .collect(Collectors.toList());

        for (Operation operation : operations){
            switch (operation.getType()){
                case ENROLLMENT:
                    initBalance = Calculate.minus(initBalance, operation.getTransactionAmount());
                    break;
                case DEBIT:
                    initBalance = Calculate.plus(initBalance, operation.getTransactionAmount());
                    break;
                case TRANSFER:
                    if(operation.getDescription().contains("Перевод")){
                        initBalance = Calculate.plus(initBalance, operation.getTransactionAmount());
                    }
                    else if(operation.getDescription().contains("Зачисление")){
                        initBalance = Calculate.minus(initBalance, operation.getTransactionAmount());
                    }
                    break;
            }
        }

        return initBalance;
    }
}
