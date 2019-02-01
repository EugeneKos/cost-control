package org.eugene.cost.logic.util;

import org.eugene.cost.logic.model.limit.SessionRepository;

import java.io.*;

public final class FileManager {
    public static void save(SessionRepository sessionRepository) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(sessionRepository);
            objectOutputStream.flush();
            writeFile(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void writeFile(byte[] content) {
        //checkFile();
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("save/sessions"))) {
            bufferedOutputStream.write(content);
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] readFile() {
        //checkFile();
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("save/sessions"))) {
            byte[] buffer = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(buffer);
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void checkFile(){
        File file = new File("save");
        if(!file.exists()){
            file.mkdir();
            // todo: Придумать что-то с этим
        }
    }

    public static SessionRepository loadSessions() {
        byte[] content = readFile();
        if (content == null) return null;
        if(content.length == 0) return null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            Object object = objectInputStream.readObject();
            if(object instanceof SessionRepository){
                return (SessionRepository)object;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
