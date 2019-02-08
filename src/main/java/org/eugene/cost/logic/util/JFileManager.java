package org.eugene.cost.logic.util;

import com.google.gson.Gson;
import org.eugene.cost.logic.model.Repository;
import org.eugene.cost.logic.model.card.bank.BankRepository;

import java.io.*;

public final class JFileManager {
    private static final String directory = "jsave";

    public static void saveRepository(Repository repository) {
        Gson gson = new Gson();
        String json = gson.toJson(repository);
        writeFile(json, repository.getName());
    }

    private static void writeFile(String content, String fileName) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(directory+File.separator+fileName+".json"))) {
            bufferedWriter.write(content);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(directory+File.separator+fileName+".json"))) {
            StringBuilder stringBuilder = new StringBuilder();
            String str;
            while ((str = bufferedReader.readLine()) != null){
                stringBuilder.append(str);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Repository loadRepository(String repositoryName) {
        String json = readFile(repositoryName);
        Gson gson = new Gson();
        return gson.fromJson(json,BankRepository.class);
    }
}
