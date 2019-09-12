package org.eugene.cost.data;

import java.time.LocalDate;

public class OperationFilter {
    private boolean increase;

    private LocalDate beginOperationsDate;
    private LocalDate finalOperationsDate;

    public OperationFilter(boolean increase, LocalDate beginOperationsDate, LocalDate finalOperationsDate) {
        this.increase = increase;
        this.beginOperationsDate = beginOperationsDate;
        this.finalOperationsDate = finalOperationsDate;
    }

    public boolean isIncrease() {
        return increase;
    }

    public LocalDate getBeginOperationsDate() {
        return beginOperationsDate;
    }

    public LocalDate getFinalOperationsDate() {
        return finalOperationsDate;
    }
}
