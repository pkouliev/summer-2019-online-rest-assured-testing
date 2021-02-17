package com.automation.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ConfigurationReader {

    private static Properties configFile;
    private static FileInputStream input;

    static {
        try {
            String path = System.getProperty("user.dir") + "/configuration.properties";
            input = new FileInputStream(path);
            configFile = new Properties();
            configFile.load(input);
            input.close();

        } catch (IOException e) {
            System.out.println("Failed to load properties file!");
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return configFile.getProperty(key);
    }
}
