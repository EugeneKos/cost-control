package org.eugene.cost.exeption;

public class NotEnoughMoneyException extends Exception {
    private static final long serialVersionUID = -930198826400748784L;

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
