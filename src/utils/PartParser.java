package utils;
import models.Part;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//public class to show function
public class PartParser {
    private static final String INVENTORY_CLEAN = "data/inventoryData.txt";

    public static List<Part> InventoryFileIntoObj() {
        List<Part> partList = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(INVENTORY_CLEAN));
            String line;

            while ((line = reader.readLine()) != null) {
                Part part = parseLine(line);
                if (part != null) {
                    partList.add(part);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return partList;
    }

    // Parsing the lines into part objects
    private static Part parseLine(String line) {
        String[] fields = line.split(",");

        if (fields.length < 6) {
            logInvalidRecord(line, "Not enough fields");
            return null;
        }

        String code = fields[0].trim();
        String name = fields[1].trim();
        String price = fields[3].trim();
        String quantity = fields[4].trim();
        String category = fields[5].trim();

        if (code.isEmpty() || name.isEmpty() || price.isEmpty() ||
                quantity.isEmpty() || category.isEmpty()) {
            logInvalidRecord(line, "Missing critical field");
            return null;
        }

        String brand = (fields.length > 2 && !fields[2].trim().isEmpty()) ? fields[2] : "unknown";
        String imgUrl = (fields.length > 7 && !fields[7].trim().isEmpty()) ? fields[7] : "no_image.png";
        String purchaseDate = (fields.length > 6 && !fields[6].trim().isEmpty()) ? fields[6].trim() : "unknown";

        category = category.toLowerCase();
        price = price.replaceAll("[^0-9.]", "");
        if (price.isEmpty() || price.equals(".")) {
            logInvalidRecord(line, "Invalid price format");
            return null;
        }
        quantity = quantity.replaceAll("[^0-9]", "");

        try {
            double parsedPrice = Double.parseDouble(price);
            int parsedQuantity = quantity.isEmpty() ? 0 : Integer.parseInt(quantity);
            return new Part(code, name, brand, parsedPrice, parsedQuantity, category, purchaseDate, imgUrl);
        } catch (NumberFormatException e) {
            logInvalidRecord(line, "Invalid numeric field: " + e.getMessage());
            return null;
        } catch (Exception e) {
            logInvalidRecord(line, "Failed to create Part: " + e.getMessage());
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

    //Save added inventory to file
    public static void saveInventoryToFIle(List<Part> parts){
        try{
            BufferedWriter writer = new BufferedWriter(new FIleWriter(INVENTORY_CLEAN));
            for (int i = 0; i <parts.size(); i++){
                Part p = parts.get(i);
                String line = p.getCode() + "," + p.getName() + "," + p.getBrand() + ","
                        + p.getPrice() + "," + p.getQty() + "," + p.getCategory() + ","
                        + p.getDateAdded() + "," + p.getImgPath();
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        }catch (IOException e){
            System.err.println("Error saving inventroy:"+ e.getMessage());
        }
    }
}
