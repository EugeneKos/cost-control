package org.eugene.cost.service;

import org.eugene.cost.data.Payment;
import org.eugene.cost.data.PaymentType;

import java.time.LocalDate;
import java.util.Set;

public interface IPaymentService {
    Payment create(String identify, String balance, PaymentType paymentType, LocalDate dateOfCreation);
    Payment create(String identify, String balance, PaymentType paymentType);

    void update(Payment payment);

    Payment getByIdentify(String identify);

    Set<Payment> getAll();
    Set<Payment> getAllByType(PaymentType paymentType);

    void delete(Payment payment);
}
