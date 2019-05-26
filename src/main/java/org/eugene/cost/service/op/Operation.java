package org.eugene.cost.service.op;

import java.io.Serializable;
import java.time.LocalDate;

public interface Operation extends Serializable {
    String execute(String balance);

    String getSum();

    LocalDate getDate();

    String getDescription();
}
