package org.eugene.cost.logic.util;

import org.eugene.cost.logic.model.Repository;

import java.io.*;

public final class FileManager {
    private static final String directory = "save";

    public static void saveRepository(Repository repository) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(repository);
            objectOutputStream.flush();
            writeFile(byteArrayOutputStream.toByteArray(), repository.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void writeFile(byte[] content, String fileName) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(directory+File.separator+fileName))) {
            bufferedOutputStream.write(content);
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] readFile(String fileName) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(directory+File.separator+fileName))) {
            byte[] buffer = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(buffer);
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Repository loadRepository(String repositoryName) {
        byte[] content = readFile(repositoryName);
        if (content == null) return null;
        if(content.length == 0) return null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            Object object = objectInputStream.readObject();
            if(object instanceof Repository){
                return (Repository) object;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
