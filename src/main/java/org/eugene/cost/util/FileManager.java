package org.eugene.cost.util;

import org.eugene.cost.data.repository.Repository;

import javax.swing.JOptionPane;
import java.io.*;

public final class FileManager {
    private static final String DIRECTORY = "save";

    private FileManager(){}

    public static void saveRepository(Repository repository) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(repository);
            objectOutputStream.flush();
            byte[] encryptedContent = Security.encrypt(byteArrayOutputStream.toByteArray());
            writeFile(encryptedContent, repository.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void writeFile(byte[] content, String fileName) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(DIRECTORY + File.separator + fileName))) {
            bufferedOutputStream.write(content);
            bufferedOutputStream.flush();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Файл с данными не найден \n" +
                            "0x0000001 [" + fileName + "]",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createFile(String fileName) throws IOException {
        String dir = DIRECTORY.contains("\\") ? DIRECTORY.replace("\\", "/") : DIRECTORY;
        String[] folders = dir.split("/");
        String path = folders[0];
        File file = new File(path);
        file.mkdir();
        for (int i = 1; i < folders.length; i++) {
            path = path + "/" + folders[i];
            file = new File(path);
            file.mkdir();
        }
        file = new File(path + "/" + fileName);
        if (!file.createNewFile()) {
            JOptionPane.showMessageDialog(null, "Ошибка создания файла \n" +
                            "0x0000002 [" + fileName + "]",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

    }

    private static byte[] readFile(String fileName) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(DIRECTORY + File.separator + fileName));
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[50];
            int len;
            while ((len = bufferedInputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Файл с данными не найден \n" +
                            "0x0000001 [" + fileName + "]",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            try {
                createFile(fileName);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Repository loadRepository(String repositoryName) {
        byte[] content = readFile(repositoryName);
        if (content == null) return null;
        if (content.length == 0) return null;
        byte[] decryptContent = Security.decrypt(content);
        if (decryptContent == null) return null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptContent);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            Object object = objectInputStream.readObject();
            if (object instanceof Repository) {
                return (Repository) object;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
