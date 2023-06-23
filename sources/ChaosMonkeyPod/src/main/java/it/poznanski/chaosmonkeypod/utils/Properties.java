package it.poznanski.chaosmonkeypod.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Properties {
    private static Properties instance;
    private static Logger logger = LogManager.getLogger(Properties.class);

    private java.util.Properties properties;
    private Properties() throws IOException{
        logger.info("Initializing properties");
        InputStream input = getClass().getClassLoader().getResourceAsStream("ChaosMonkeyPod.properties");
        this.properties = new java.util.Properties();
        properties.load(input);
    }
    public static synchronized Properties getInstance() throws IOException{
        if (instance == null) {
            instance = new Properties();
        }
        return instance;
    }

    public java.util.Properties getProperties() {
        return properties;
    }
}
