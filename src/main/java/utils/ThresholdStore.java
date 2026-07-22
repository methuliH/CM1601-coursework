package utils;

import models.Part;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThresholdStore {
    private static final String THRESHOLD_FILE = "data/lowStockThresholds.txt";

    public static void applyThresholds(List<Part> parts) {
        List<String> lines = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(THRESHOLD_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException e) {
            return;
        }

        for (int i = 0; i < lines.size(); i++) {
            String[] fields = lines.get(i).split(",");
            if (fields.length < 2) {
                continue;
            }

            String code = fields[0].trim();
            String thresholdStr = fields[1].trim();

            try {
                int threshold = Integer.parseInt(thresholdStr);
                for (int j = 0; j < parts.size(); j++) {
                    if (parts.get(j).getCode().equals(code)) {
                        parts.get(j).setLowStockThreshold(threshold);
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid threshold value for " + code + ": " + thresholdStr);
            }
        }
    }

    // Rewrites the whole threshold file - one "code,threshold" line per part.
    public static void saveThresholds(List<Part> parts) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(THRESHOLD_FILE));
            for (int i = 0; i < parts.size(); i++) {
                Part p = parts.get(i);
                String line = p.getCode() + "," + p.getLowStockThreshold();
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving low-stock thresholds: " + e.getMessage());
        }
    }
}
