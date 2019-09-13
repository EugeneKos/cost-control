package org.eugene.cost.file.encryption;

import org.eugene.cost.exeption.EncryptionException;

public interface EncryptionService<T> {
    void setSecretKey(String secretKey);

    T encrypt(T object) throws EncryptionException;

    T decrypt(T object) throws EncryptionException;
}
