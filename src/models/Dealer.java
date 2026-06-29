package models;

public class Dealer {
    private String id;
    private String name;
    private String phone;
    private String location;

    public Dealer(String id, String name, String phone, String location){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.location = location;
    }

    public String getId(){return id;}
    public String getName(){return name;}
    public String getPhone(){return phone;}
    public String location(){return location;}

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
