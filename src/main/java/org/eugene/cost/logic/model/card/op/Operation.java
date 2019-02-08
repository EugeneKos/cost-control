package org.eugene.cost.logic.model.card.op;

import java.io.Serializable;
import java.time.LocalDate;

public interface Operation extends Serializable {
    String execute(String balance);

    String getSum();

    LocalDate getDate();
}
