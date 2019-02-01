package org.eugene.cost.logic.model.card.op;

public interface Operation {
    String execute(String balance);

    String getDescription();
}
