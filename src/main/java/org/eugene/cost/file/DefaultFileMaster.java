package org.eugene.cost.file;

import org.apache.log4j.Logger;
import org.eugene.cost.exeption.EncryptionException;
import org.eugene.cost.file.encryption.EncryptionService;
import org.eugene.cost.service.util.PropertyLoader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DefaultFileMaster<T> {
    private static Logger LOGGER = Logger.getLogger(DefaultFileMaster.class);

    private EncryptionService<byte[]> encryptionService;

    public DefaultFileMaster(EncryptionService<byte[]> encryptionService) {
        this.encryptionService = encryptionService;
    }

    protected void saveObject(T object, String fullFileName) {
        boolean dataEncryption = Boolean.valueOf(PropertyLoader.getProperty("data.encryption"));

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
        ) {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            byte[] content = byteArrayOutputStream.toByteArray();

            if(dataEncryption){
                content = encryptionService.encrypt(content);
            }

            writeInFile(content, fullFileName);
        } catch (IOException | EncryptionException e) {
            LOGGER.error(e);
        }

    }

    private void writeInFile(byte[] content, String fullFileName) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                new FileOutputStream(fullFileName)
        )) {
            bufferedOutputStream.write(content);
            bufferedOutputStream.flush();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    protected T loadObject(String fullFileName, Class<T> clazz) {
        boolean dataEncryption = Boolean.valueOf(PropertyLoader.getProperty("data.encryption"));

        byte[] content = readFromFile(fullFileName);
        if (content == null || content.length == 0){
            return null;
        }

        if(dataEncryption){
            try {
                content = encryptionService.decrypt(content);
            } catch (EncryptionException e) {
                LOGGER.error(e);
            }
        }

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)
        ) {
            return clazz.cast(objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error(e);
        }
        return null;
    }

    private byte[] readFromFile(String fullFileName) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(
                new FileInputStream(fullFileName));

             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            byte[] buffer = new byte[50];
            int len;
            while ((len = bufferedInputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return null;
    }
}
