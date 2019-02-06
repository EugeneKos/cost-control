package org.eugene.cost.logic.model.card.op;

import java.io.Serializable;

public interface Operation extends Serializable {
    String execute(String balance);

    String getDescription();
}
