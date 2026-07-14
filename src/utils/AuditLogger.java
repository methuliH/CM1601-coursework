package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {
    private static final String AUDIT_LOG_FILE = "data/audit_log.txt";

    // Writes one line to audit_log.txt, never overwriting previous entries (append-only)
    public static void log(String action, String partCode, int quantity) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(AUDIT_LOG_FILE, true));

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            String logLine = timestamp + " | " + action + " | " + partCode + " | qty: " + quantity;

            writer.write(logLine);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to audit log: " + e.getMessage());
        }
    }
}