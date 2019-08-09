package org.eugene.cost.cache;

import org.eugene.cost.data.Bank;
import org.eugene.cost.data.Card;
import org.eugene.cost.data.Cash;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BankCache {
    private Map<String, Bank> bankCache = new HashMap<>();

    public void addBank(Bank bank){
        String identify;
        if(bank instanceof Card){
            identify = ((Card) bank).getNumber();
        } else {
            identify = ((Cash) bank).getDescription();
        }
        if(!bankCache.containsValue(bank)){
            bankCache.put(identify, bank);
        }
    }

    public Bank getBank(String identify){
        return bankCache.get(identify);
    }
}
