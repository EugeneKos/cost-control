package org.eugene.cost.cache;

import org.eugene.cost.data.Payment;
import org.eugene.cost.file.FileManager;
import org.eugene.cost.service.util.PaymentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PaymentCache {
    private Map<String, Payment> paymentCache = new HashMap<>();

    private FileManager<Payment> fileManager;

    @Autowired
    public PaymentCache(FileManager<Payment> fileManager) {
        this.fileManager = fileManager;
    }

    public void addPayment(Payment payment){
        String identify = payment.getIdentify();
        paymentCache.put(identify, payment);
    }

    public Payment getPayment(String identify){
        return paymentCache.get(identify);
    }

    public void deletePayment(Payment payment){
        String identify = payment.getIdentify();
        paymentCache.remove(identify);
    }

    public Collection<Payment> getAllPayments(){
        return paymentCache.values();
    }

    public void initialize(){
        Collection<Payment> payments = fileManager.loadAll(PaymentUtils.PAYMENT_REGEXP, Payment.class).stream()
                .filter(Objects::nonNull).collect(Collectors.toList());

        payments.forEach(this::addPayment);
    }
}
