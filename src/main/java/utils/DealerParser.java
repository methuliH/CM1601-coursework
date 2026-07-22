package utils;

import models.Dealer;
import java.util.List;



public class DealerParser extends utils.LegacyRecordParser<Dealer> {
    private static final String DEALER_CLEAN = "data/dealerData.txt";

    public static List<Dealer> dealerFileIntoObj() {
        return new DealerParser().parseFile(DEALER_CLEAN);
    }

    // Parsing the lines into dealer objects
    @Override
    protected Dealer parseLine(String line) {
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
            return new Dealer(code, name, telephone, location);
        } catch (Exception e) {
            logInvalidRecord(line, "Failed to create Dealer: " + e.getMessage());
            return null;
        }
    }
}
