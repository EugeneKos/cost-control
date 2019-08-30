package org.eugene.cost.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface FileManager<T> {
    String WORK_DIRECTORY = System.getProperty("user.dir") + File.separator + "save";

    default Set<String> searchFiles(String regexp){
        File file = new File(WORK_DIRECTORY);
        File[] files = file.listFiles();
        if(files == null){
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(files)).stream()
                .filter(f -> f.getName().matches(regexp))
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    Collection<T> loadAll(String regexpFileNames, Class<T> clazz);
    T load(String fileName, Class<T> clazz);

    void save(T object, String fileName);

    default void delete(String fileName){
        Path path = Paths.get(WORK_DIRECTORY + File.separator + fileName);
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
