package org.eugene.cost.logic.model.card.bank;

import java.util.HashSet;
import java.util.Set;

public class BankRepository {
    private Set<Bank> banks = new HashSet<>();

    public void addBank(Bank bank){
        banks.add(bank);
    }

    public void removeBank(Bank bank){
        banks.remove(bank);
    }

    public Bank getBank(){
        throw new RuntimeException("temporarily not working");
    }

    public Set<Bank> getBanks() {
        return banks;
    }
}
