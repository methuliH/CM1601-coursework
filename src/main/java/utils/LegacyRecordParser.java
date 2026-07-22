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

public abstract class LegacyRecordParser<T> {

    protected static final String INVALID_RECORDS_FILE = "data/invalidRecords.txt";


    protected abstract T parseLine(String line);

    // open the file, read every line, parse it, collect the valid ones
    public List<T> parseFile(String filePath) {
        List<T> results = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                T record = parseLine(line);
                if (record != null) {
                    results.add(record);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return results;
    }

    // log a line that couldn't be turned into a record
    protected void logInvalidRecord(String line, String reason) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(INVALID_RECORDS_FILE, true));
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            String logEntry = timestamp + " | " + reason + " | " + line;
            writer.write(logEntry);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.err.println("Unable to log invalid record: " + e);
        }
    }
}