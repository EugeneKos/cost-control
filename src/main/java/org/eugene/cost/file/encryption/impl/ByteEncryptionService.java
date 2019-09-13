package org.eugene.cost.file.encryption.impl;

import org.apache.log4j.Logger;
import org.eugene.cost.exeption.EncryptionException;
import org.eugene.cost.file.encryption.EncryptionService;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
public class ByteEncryptionService implements EncryptionService<byte[]> {
    private static Logger LOGGER = Logger.getLogger(ByteEncryptionService.class);

    private String secretKey;

    @Override
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public byte[] encrypt(byte[] content) throws EncryptionException {
        try {

            SecretKey secretKey = generateKey();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(content);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {

            LOGGER.error("Ошибка шифрования данных", e);
            throw new EncryptionException("Ошибка шифрования данных", e);
        }
    }

    @Override
    public byte[] decrypt(byte[] content) throws EncryptionException {
        try {

            SecretKey secretKey = generateKey();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(content);

        } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                InvalidKeyException | BadPaddingException  |
                IllegalBlockSizeException e) {

            LOGGER.error("Ошибка расшифровки данных", e);
            throw new EncryptionException("Ошибка расшифровки данных", e);
        }
    }

    private SecretKey generateKey() throws EncryptionException {
        if(StringUtils.isEmpty(secretKey)){
            throw new EncryptionException("Секретный ключ не задан");
        }
        byte[] keyBytes = Arrays.copyOf(secretKey.getBytes(),16);
        return new SecretKeySpec(keyBytes, "AES");
    }
}
