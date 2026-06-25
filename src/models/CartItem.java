package models;

public class CartItem {
    private int quantity;
    private Part part;

    public CartItem(int quantity,Part part){
        this.quantity = quantity;
        this.part = part;
    }
    public int getQuantity(){
        return quantity;
    }

    public Part getPart() {
        return part;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
