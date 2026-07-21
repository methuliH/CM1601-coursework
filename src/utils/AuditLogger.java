package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import models.AuditLogEntry;

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

    public static List<AuditLogEntry> readAllEntries() {
        List<AuditLogEntry> entries = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(AUDIT_LOG_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length < 4) continue;
                String timestamp = parts[0].trim();
                String action = parts[1].trim();
                String partCode = parts[2].trim();
                int quantity = 0;
                try {
                    quantity = Integer.parseInt(parts[3].replace("qty:", "").trim());
                } catch (NumberFormatException ignored) {}
                entries.add(new AuditLogEntry(timestamp, action, partCode, quantity));
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading audit log: " + e.getMessage());
        }
        return entries;
    }
}