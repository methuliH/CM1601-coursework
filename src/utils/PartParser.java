package utils;
import models.Part;
import java.util.List;
import java.util.ArrayList;


public class PartParser {
    private static final String INVENTORY_CLEAN = "data/inventoryData.txt";

    public static List<Part> InventoryFileIntoObj(){
        return List<Part>;
    }

    private static Part parseline(String line) {
        String[] fields = line.split(",");

        if (s.length < 6) {
            logInvalidRecord(line, "Not enough fields");
            return null;
        }

        String code = fields[0].trim();
        String name = fields[1].trim();
        String price = fields[3].trim();
        String quantity = fields[4].trim();
        String category = fields[5].trim();


        //Check if any fields are critical fields are empty
        if (code.isEmpty() || name.isEmpty() || price.isEmpty() ||
                quantity.isEmpty() || category.isEmpty()) {
            logInvalidRecord(line, "Missing critical field");
            return null;
        }

        //Extract optional fields
        String brand = (fields.length > 2 && !fields[2].trim().isEmpty()) ?fields[2]: "unknown";
        String imgUrl = (fields.length > 7 && !fields[7].trim().isEmpty()) ?fields[7]: "no_image.png";
        String purchaseDate = (fields.length > 6 && !fields[6].trim().isEmpty()) ?fields[6].trim() : "unknown";

        //Normalize categories
        category = category.toLowerCase();
        price = price.replaceAll("[^0-9.]", "");
        quantity = quantity.replaceAll("[^0-9]", "");

        try{
            Part part = new Part(code,name,brand,price,quantity,category,purchaseDate,imgUrl);
            return part;
        } catch(Exception e){
            logInvalidRecord(line, "Failed to create Part: " + e.getMessage());
            return null;
        }

        //add log invalid records





    }
}
