package org.eugene.cost.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class PropertyLoader {
    private static final String DIRECTORY = "config";
    private static final String FILE_NAME = "config.properties";

    private static Properties properties = new Properties();

    static {
        try {
            properties.load(new FileInputStream(DIRECTORY + File.separator + FILE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PropertyLoader() {}

    public static String getProperty(String name){
        return properties.getProperty(name);
    }
}
