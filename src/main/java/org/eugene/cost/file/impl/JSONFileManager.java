package org.eugene.cost.file.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eugene.cost.file.FileManager;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JSONFileManager<T> implements FileManager<T> {
    private static final String EXT = ".json";

    @Override
    public Collection<T> loadAll(String regexpFileNames, Class<T> clazz) {
        regexpFileNames += EXT;
        Set<String> fileNames = searchFiles(regexpFileNames);
        return fileNames.stream().map(fileName -> load(fileName, clazz)).collect(Collectors.toList());
    }

    @Override
    public T load(String fileName, Class<T> clazz) {
        Gson gson = getGson();
        String json = read(fileName);
        return gson.fromJson(json, clazz);
    }

    @Override
    public void save(T object, String fileName) {
        Gson gson = getGson();
        String json = gson.toJson(object);
        write(json, fileName);
    }

    private Gson getGson(){
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    private void write(String json, String fileName){
        fileName = correctedAndGetFileName(fileName);
        try (Writer writer = new FileWriter(new File(WORK_DIRECTORY + File.separator + fileName + EXT))) {
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String read(String fileName){
        fileName = correctedAndGetFileName(fileName);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(
                new FileReader(new File(WORK_DIRECTORY + File.separator + fileName + EXT)))
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null){
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private String correctedAndGetFileName(String fullFileName){
        if(fullFileName.contains(EXT)){
            return fullFileName.replace(EXT, "");
        }
        return fullFileName;
    }
}
