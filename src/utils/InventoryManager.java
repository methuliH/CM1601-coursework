package utils;

import models.Part;
import java.util.List;
import java.util.ArrayList;

public class InventoryManager {
    private List<Part> parts;
    private static final int DEFAULT_LOW_STOCK_THRESHOLD = 5; //This value do not change

    public InventoryManager(List<Part> parts){
        this.parts = parts;
    }

    //Check if part code is available
    private boolean partCodeExists(String code) {
        for (int i = 0; i < this.parts.size(); ++i) {
            if (this.parts.get(i).getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    //Total quantity of parts
    public int getTotalItemCount() {
        int totalQty = 0;
        for (int i = 0; i < this.parts.size(); ++i) {
            totalQty = totalQty + this.parts.get(i).getQuantity();
        }
        return totalQty;
    }

    //Total inventory value
    public double getTotalInventoryValue() {
        double totalSum = 0;
        for(int i=0; i<this.parts.size(); ++i){
            double priceValue = this.parts.get(i).getPrice();
            double quantity = this.parts.get(i).getQty();

            totalSum = totalSum + (priceValue*quantity);
        }
        return totalSum;  // placeholder
    }

    //Get parts with low stock
    public List<Part> getLowStockItems() {
        List<Part> lowStockParts = new ArrayList<>();

        for (int i = 0; i < this.parts.size(); ++i) {
            if (this.parts.get(i).getQty() < DEFAULT_LOW_STOCK_THRESHOLD){
                lowStockParts.add(this.parts.get(i));
            }
        }
        return lowStockParts;  // placeholder
    }

    // TODO: Sorting methods (signatures only)

    //Sort using category
    private List<Part> sortByCodeWithinCategory(List<Part> parts) {
        int n = parts.size();

        for (int pass = 0; pass < n - 1; pass++) {
            for (int i = 0; i < n - 1 - pass; i++) {

                String code1 = parts.get(i).getCode();
                String code2 = parts.get(i + 1).getCode();

                // extract numbers from codes and compare
                int num1 = Integer.parseInt(code1.substring(1));
                int num2 = Integer.parseInt(code2.substring(1));

                // If first code number > second code number, swap them
                if (num1 > num2) {
                    Part temp = parts.get(i);
                    parts.set(i, parts.get(i + 1));
                    parts.set(i + 1, temp);
                }
            }
        }

        return parts;
    }


    //Sort using category
    public List<Part> getAllPartsSortedByCategory() {
        ArrayList<String> uniqueCategories = new ArrayList<>();

        for (int i = 0; i < this.parts.size(); i++) {
            String category = this.parts.get(i).getCategory();

            boolean categoryExists = false;
            for (int j = 0; j < uniqueCategories.size(); j++) {
                if (uniqueCategories.get(j).equals(category)) {
                    categoryExists = true;
                    break;
                }
            }

            if (!categoryExists) {
                uniqueCategories.add(category);
            }
        }

        ArrayList<Part> result = new ArrayList<>();

        for (int i = 0; i < uniqueCategories.size(); i++) {
            String currentCategory = uniqueCategories.get(i);
            ArrayList<Part> partsInCategory = new ArrayList<>();

            for (int j = 0; j < this.parts.size(); j++) {
                if (this.parts.get(j).getCategory().equals(currentCategory)) {
                    partsInCategory.add(this.parts.get(j));
                }
            }

            partsInCategory = (ArrayList<Part>) sortByCodeWithinCategory(partsInCategory);

            for (int j = 0; j < partsInCategory.size(); j++) {
                result.add(partsInCategory.get(j));
            }
        }

        return result;
    }



    //Add new objects
    public void addPart(Part part) {
        if (partCodeExists(part.getCode())){
            System.out.println("Code unavailable");
            return;
        }
        this.parts.add(part);
        System.out.println("part "+ part.getCode() + " added successfully");
    }

    public void updatePart(String code, Part updated) {
        for(int i = 0; i < this.parts.size(); i++){
            if (this.parts.get(i).getCode().equals(code)) {
                this.parts.set(i, updated);
                System.out.println("Part " + code + " updated successfully.");
                return;
            }
        }
        System.out.println("Error: Part code " + code + " not found.");
    }

    //delete a part
    public void deletePart(String code) {
        for(int i = 0; i < this.parts.size(); i++){
            if (this.parts.get(i).getCode().equals(code)){
                this.parts.remove(i);
                System.out.println("Part " + code + " deleted successfully.");
                return;
            }
        }
        System.out.println("Error: Part code " + code + " not found.");
    }

    public List<Part> search(String keyword, String category, double minPrice, double maxPrice) {
        List<Part> results = new ArrayList<>();

        for (int i = 0; i < this.parts.size(); i++) {
            Part part = this.parts.get(i);

            boolean keywordMatch = part.getName().toLowerCase().contains(keyword.toLowerCase());

            boolean categoryMatch = true;
            if (category != null && !category.isEmpty()) {
                categoryMatch = part.getCategory().toLowerCase().equals(category.toLowerCase());
            }

            boolean priceMatch = part.getPrice() >= minPrice && part.getPrice() <= maxPrice;


            if (keywordMatch && categoryMatch && priceMatch) {
                results.add(part);
            }
        }

        return results;
    }
}
