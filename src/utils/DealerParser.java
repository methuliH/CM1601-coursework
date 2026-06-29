package utils;
import models.Dealer;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DealerParser {
    private static final String DEALER_CLEAN = "data/dealerData.txt";

    public static List<Dealer> dealerFileIntoObj() {
        List<Dealer> dealerList = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(DEALER_CLEAN));
            String line;

            while ((line = reader.readLine()) != null) {
                Dealer dealer = parseLine(line);
                if (dealer != null) {
                    dealerList.add(dealer);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return dealerList;
    }

    // Parsing the lines into part objects
    private static Dealer parseLine(String line) {
        String[] fields = line.split(",");

        if (fields.length < 4) {
            logInvalidRecord(line, "Not enough fields");
            return null;
        }

        String code = fields[0].trim();
        String name = fields[1].trim();
        String location = fields[3].trim();

        if (code.isEmpty() || name.isEmpty() || location.isEmpty()) {
            logInvalidRecord(line, "Missing critical field");
            return null;
        }

        String telephone = (fields.length > 2 && !fields[2].trim().isEmpty()) ? fields[2] : "unknown";

        telephone = telephone.replaceAll("[^0-9.]", "");


        try {
            return new Dealer(code, name, telephone,location);
        } catch (Exception e) {
            logInvalidRecord(line, "Failed to create Dealer: " + e.getMessage());
            return null;
        }
    }
    //Logging invalid records
    private static void logInvalidRecord(String line, String reason) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/invalidRecords.txt", true));
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



}
