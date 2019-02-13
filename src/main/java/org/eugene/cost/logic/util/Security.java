package org.eugene.cost.logic.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public final class Security {
    private Security(){}

    public static byte[] encrypt(byte[] content){
        try {
            SecretKey secretKey = generateKey();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(content);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                BadPaddingException  | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] encryptedBytes){
        try {
            SecretKey secretKey = generateKey();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(encryptedBytes);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                InvalidKeyException | BadPaddingException  |
                IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SecretKey generateKey(){
        String key = "keY4PasSwoRd9751";
        byte[] keyBytes = Arrays.copyOf(key.getBytes(),16);
        return new SecretKeySpec(keyBytes, "AES");
    }
}
