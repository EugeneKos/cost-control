package org.eugene.cost.service.util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public final class PropertyLoader {
    private static Logger LOGGER = Logger.getLogger(PropertyLoader.class);

    private static final String PROPERTIES_FILE = "config/cost-control-config.properties";

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(new BufferedReader(new FileReader(new File(PROPERTIES_FILE))));
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    private PropertyLoader(){}

    public static String getProperty(String name){
        return properties.getProperty(name);
    }
}
