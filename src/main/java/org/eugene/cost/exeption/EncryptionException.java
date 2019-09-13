package org.eugene.cost.exeption;

public class EncryptionException extends Exception {
    private static final long serialVersionUID = -4156557307167030239L;

    public EncryptionException(String message) {
        super(message);
    }

    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
