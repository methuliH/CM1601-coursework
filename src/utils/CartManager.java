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

        if (foundPart == null) {
            System.out.println("Invalid part code");
            return;
        }

        if (foundPart.getQty() < quantity) {
            System.out.println("Not enough stock available");
            return;
        }

        for (int i = 0; i < this.items.size(); i++) {
            if (this.items.get(i).getPart().getCode().equals(partCode)) {
                this.items.get(i).setQuantity(this.items.get(i).getQuantity() + quantity);
                System.out.println("Updated cart item quantity.");
                return;
            }
        }

        this.items.add(new CartItem(quantity, foundPart));
        System.out.println("Added to cart successfully.");
    }

    // Remove item from cart
    public void removeItem(String partCode) {
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

    // Calculate subtotal without discounts
    public double calculateSubTotal() {
        double subTotal = 0.0;
        for (int i = 0; i < this.items.size(); i++) {
            int qty = this.items.get(i).getQuantity();
            double price = this.items.get(i).getPart().getPrice();
            subTotal += qty * price;
        }
        return subTotal;
    }

    // Calculate bulk discount (5% if qty >= 3)
    public double calculateBulkDiscount() {
        double discountPercent = 0.05;
        int bulkDiscountAvailableQty = 3;
        double bulkDiscount = 0.0;

        for (int i = 0; i < this.items.size(); i++) {
            if (this.items.get(i).getQuantity() >= bulkDiscountAvailableQty) {
                int qty = this.items.get(i).getQuantity();
                double price = this.items.get(i).getPart().getPrice();
                double itemSubTotal = qty * price;
                bulkDiscount += itemSubTotal * discountPercent;
            }
        }
        return bulkDiscount;
    }

    // Check if cart has both Engine and Electrical parts
    private boolean hasBothEngineAndElectrical() {
        boolean hasEngine = false;
        boolean hasElectrical = false;

        for (int i = 0; i < this.items.size(); i++) {
            String category = this.items.get(i).getPart().getCategory().toLowerCase();

            if (category.equals("engine")) {
                hasEngine = true;
            }
            if (category.equals("electrical")) {
                hasElectrical = true;
            }
        }
        return hasEngine && hasElectrical;
    }

    // Calculate synergy discount (10% if Engine + Electrical)
    public double calculateSynergyDiscount(double subtotalAfterBulk) {
        if (hasBothEngineAndElectrical()) {
            return subtotalAfterBulk * 0.10;  // 10% discount
        }
        return 0.0;
    }

    // Calculate final total after all discounts
    public double calculateTotal() {
        double subtotal = calculateSubTotal();
        double bulkDiscount = calculateBulkDiscount();
        double synergyDiscount = calculateSynergyDiscount(subtotal - bulkDiscount);

        return subtotal - bulkDiscount - synergyDiscount;
    }

    // Get all items in cart
    public List<CartItem> getItems() {
        return this.items;
    }


    public boolean processCheckout() {
        if (this.items.isEmpty()) {
            System.out.println("Cart is empty");
            return false;
        }

        for (int i = 0; i < this.items.size(); i++) {
            CartItem item = this.items.get(i);
            if (item.getQuantity() > item.getPart().getQty()) {
                System.out.println("Not enough stock for " + item.getPart().getCode());
                return false;
            }
        }

        for (int i = 0; i < this.items.size(); i++) {
            CartItem item = this.items.get(i);
            Part part = item.getPart();
            part.setQty(part.getQty() - item.getQuantity());
            AuditLogger.log("CHECKOUT", part.getCode(), item.getQuantity());
        }

        PartParser.saveInventoryToFile(this.inventory.getAllParts());
        this.items.clear();
        return true;
    }
}