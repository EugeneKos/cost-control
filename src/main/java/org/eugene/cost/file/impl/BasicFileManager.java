package org.eugene.cost.file.impl;

import org.eugene.cost.file.DefaultFileMaster;
import org.eugene.cost.file.FileManager;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BasicFileManager<T> extends DefaultFileMaster<T> implements FileManager<T> {
    @Override
    public Collection<T> loadAll(String regexpFileNames, Class<T> clazz) {
        Set<String> fileNames = searchFiles(regexpFileNames);
        return fileNames.stream().map(fileName -> load(fileName, clazz)).collect(Collectors.toList());
    }

    @Override
    public T load(String fileName, Class<T> clazz) {
        return loadObject(getFullFileName(fileName), clazz);
    }

    @Override
    public void save(T object, String fileName) {
        saveObject(object, getFullFileName(fileName));
    }

    private String getFullFileName(String fileName){
        return WORK_DIRECTORY + File.separator + fileName;
    }
}
