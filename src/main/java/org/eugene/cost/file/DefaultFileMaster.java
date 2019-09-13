package org.eugene.cost.file;

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
    protected void saveObject(T object, String fullFileName) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
        ) {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            byte[] content = byteArrayOutputStream.toByteArray();
            writeInFile(content, fullFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeInFile(byte[] content, String fullFileName) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                new FileOutputStream(fullFileName)
        )) {
            bufferedOutputStream.write(content);
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected T loadObject(String fullFileName, Class<T> clazz) {
        byte[] content = readFromFile(fullFileName);
        if (content == null || content.length == 0){
            return null;
        }
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)
        ) {
            return clazz.cast(objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return null;
    }
}
