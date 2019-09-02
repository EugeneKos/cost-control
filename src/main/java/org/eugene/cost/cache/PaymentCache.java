package org.eugene.cost.cache;

import org.eugene.cost.data.Payment;
import org.eugene.cost.data.Card;
import org.eugene.cost.data.Cash;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentCache {
    private Map<String, Payment> bankCache = new HashMap<>();

    public void addPayment(Payment payment){
        String identify;
        if(payment instanceof Card){
            identify = ((Card) payment).getNumber();
        } else {
            identify = ((Cash) payment).getDescription();
        }
        if(!bankCache.containsValue(payment)){
            bankCache.put(identify, payment);
        }
    }

    public Payment getPayment(String identify){
        return bankCache.get(identify);
    }
}
