package com.hotel.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

public class DBConnection {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Load db.yaml
                Yaml yaml = new Yaml();
                InputStream inputStream = DBConnection.class.getClassLoader()
                        .getResourceAsStream("db.yaml");
                Map<String, Object> obj = yaml.load(inputStream);

                Map<String, String> dbConfig = (Map<String, String>) obj.get("datasource");
                String url = dbConfig.get("url");
                String username = dbConfig.get("username");
                String password = dbConfig.get("password");

                // Connect DB
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Kết nối database thành công!");
            } catch (Exception e) {
                System.out.println("Kết nối database thất bại!");
                e.printStackTrace();
            }
        }
        return connection;
    }
}
