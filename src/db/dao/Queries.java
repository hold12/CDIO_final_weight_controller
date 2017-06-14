package db.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.text.MessageFormat;

class Queries {
    static String getSQL(String propertyName) {
        File file = null;
        Properties properties = null;
        URL url = Queries.class.getClassLoader().getResource("sql.properties");

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        }

        try (FileInputStream fileInputStream = new FileInputStream(file)){
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.err.println("Failed to read file: \"resources/sql.properties\".");
        }
        return properties.getProperty(propertyName);
    }
    static String getFormatted(String propertyName, String... args) {
        return MessageFormat.format(getSQL(propertyName), (Object[]) args);
    }
}