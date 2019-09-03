package org.eugene.cost.service.impl;

import org.eugene.cost.cache.PaymentCache;
import org.eugene.cost.data.Payment;
import org.eugene.cost.data.PaymentType;
import org.eugene.cost.file.FileManager;
import org.eugene.cost.service.IPaymentService;
import org.eugene.cost.service.util.PaymentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements IPaymentService {
    private PaymentCache paymentCache;

    private FileManager<Payment> fileManager;

    @Autowired
    public PaymentServiceImpl(PaymentCache paymentCache,
                              @Qualifier("basicFileManager") FileManager<Payment> fileManager) {

        this.paymentCache = paymentCache;
        this.fileManager = fileManager;
    }

    @Override
    public Payment create(String identify, String balance, PaymentType paymentType, LocalDate dateOfCreation) {
        return createPayment(identify, balance, paymentType, dateOfCreation);
    }

    @Override
    public Payment create(String identify, String balance, PaymentType paymentType) {
        return createPayment(identify, balance, paymentType, null);
    }

    private Payment createPayment(String identify, String balance, PaymentType paymentType, LocalDate dateOfCreation){
        Payment payment = paymentCache.getPayment(identify);
        if(payment != null){
            return payment;
        }
        if(dateOfCreation == null){
            payment = new Payment(identify, balance, paymentType);
        } else {
            payment = new Payment(identify, balance, paymentType, dateOfCreation);
        }
        paymentCache.addPayment(payment);
        update(payment);
        return payment;
    }

    @Override
    public void update(Payment payment) {
        fileManager.save(payment, PaymentUtils.getPaymentFileName(payment.getIdentify()));
    }

    @Override
    public Set<Payment> getAll() {
        return new HashSet<>(paymentCache.getAllPayments());
    }

    @Override
    public Set<Payment> getAllByType(PaymentType paymentType) {
        return getAll().stream()
                .filter(payment -> payment.getPaymentType() == paymentType)
                .collect(Collectors.toSet());
    }

    @Override
    public void delete(Payment payment) {
        fileManager.delete(PaymentUtils.getPaymentFileName(payment.getIdentify()));
        paymentCache.deletePayment(payment);
    }
}
