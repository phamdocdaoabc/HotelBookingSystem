package com.hotel.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtil {
    private static final String LOG_FILE = "logs/history_log.txt";

    public static void log(String action) {
        try {
            File dir = new File("logs");
            if (!dir.exists()) {
                dir.mkdirs(); // Tạo thư mục nếu chưa có
            }

            FileWriter writer = new FileWriter(LOG_FILE, true);
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + time + "] " + action + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
