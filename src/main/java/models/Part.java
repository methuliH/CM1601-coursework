package models;

public class Part {
    private String code;
    private String name;
    private String brand;
    private double price;
    private int quantity;
    private String category;
    private String dateAdded;
    private String imgPath;
    private int lowStockThreshold = 5;

    //Making an object
    public Part(String code, String name, String brand, double price, int quantity, String category,String dateAdded, String imgPath){
        this.code = code;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.dateAdded = dateAdded;
        this.imgPath = imgPath;
    }

    //Getters
    public String getCode(){
        return code;
    }
    public String getName(){
        return name;
    }
    public String getBrand(){
        return brand;
    }
    public double getPrice(){
        return price;
    }
    public int getQty(){
        return quantity;
    }
    public String getCategory(){
        return category;
    }
    public String getDateAdded(){
        return dateAdded;
    }
    public String getImgPath(){
        return imgPath;
    }
    public int getLowStockThreshold(){return lowStockThreshold;}

    //Setters
    public void setName(String name){this.name = name;}
    public void setPrice(double price){this.price=price;}
    public void setQty(int quantity){this.quantity=quantity;}
    public void setCategory(String category){this.category=category;}
    public void setLowStockThreshold(int lowStockThreshold){this.lowStockThreshold=lowStockThreshold;}
}
