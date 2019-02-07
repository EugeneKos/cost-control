package org.eugene.cost.logic.model.card.bank;

import org.eugene.cost.logic.model.Repository;

import java.util.HashSet;
import java.util.Set;

public class BankRepository implements Repository {
    private Set<Bank> banks = new HashSet<>();

    public boolean addBank(Bank bank){
        return banks.add(bank);
    }

    public boolean removeBank(Bank bank){
        return banks.remove(bank);
    }

    public Set<Bank> getBanks() {
        return banks;
    }

    @Override
    public String getName() {
        return "banks";
    }
}
