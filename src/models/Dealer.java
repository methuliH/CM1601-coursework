package models;

public class Dealer {
    private int id;
    private String name;
    private int phone;
    private String location;

    public Dealer(int id, String name, int phone, String location){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.location = location;
    }

    public int getId(){return id;}
    public String getName(){return name;}
    public int getPhone(){return phone;}
    public String location(){return location;}

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
