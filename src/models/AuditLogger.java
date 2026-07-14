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
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    String timestamp = parts[0].trim();
                    String action = parts[1].trim();
                    String code = parts[2].trim();
                    String qtyText = parts[3].trim().replace("qty:", "").trim();

                    int qty = 0;
                    try {
                        qty = Integer.parseInt(qtyText);
                    } catch (NumberFormatException e) {
                        // skip malformed quantity, keep qty as 0
                    }

                    entries.add(new AuditLogEntry(timestamp, action, code, qty));
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("No audit log yet, or error reading it: " + e.getMessage());
        }

        return entries;
    }
}