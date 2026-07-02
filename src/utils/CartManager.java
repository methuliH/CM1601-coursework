package utils;

import models.CartItem;
import models.Part;
import java.util.List;
import java.util.ArrayList;

public class CartManager {
    private List<CartItem> items;
    private InventoryManager inventory;

    public CartManager(InventoryManager inventory) {
        this.items = new ArrayList<>();
        this.inventory = inventory;
    }

    // Add item to cart
    public void addItem(String partCode, int quantity) {

        if (quantity <= 0) {
            System.out.println("Error: Quantity must be greater than 0.");
            return;
        }


        Part foundPart = null;
        for (int i = 0; i < this.inventory.getAllParts().size(); i++) {
            Part part = this.inventory.getAllParts().get(i);
            if (partCode.equals(part.getCode())) {
                foundPart = part;
                break;
            }
        }

        // Check if part exists
        if (foundPart == null) {
            System.out.println("Invalid part code");
            return;
        }

        // Check if enough stock
        if (foundPart.getQty() < quantity) {
            System.out.println("Not enough stock available");
            return;
        }

        // Check if part already in cart
        for (int i = 0; i < this.items.size(); i++) {
            if (this.items.get(i).getPart().getCode().equals(partCode)) {
                // Update quantity if already in cart
                this.items.get(i).setQuantity(this.items.get(i).getQuantity() + quantity);
                System.out.println("Updated cart item quantity.");
                return;
            }
        }

        // Add new item to cart
        this.items.add(new CartItem(quantity, foundPart));
        System.out.println("Added to cart successfully.");
    }

    //remove items
    public void removeItem(String partCode){
        //check if item is in cart
        for (int i = 0; i < this.items.size(); i++) {
            String itemCode = this.items.get(i).getPart().getCode();
            if (itemCode.equals(partCode)) {
                this.items.remove(i);
                System.out.println("Item removed from cart.");
                return;
            }
        }
        System.out.println("PartCode not found");
    }

    //discounts





    // Get all items in cart
    public List<CartItem> getItems() {
        return this.items;
    }

}