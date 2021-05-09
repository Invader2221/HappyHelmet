package com.example.happy_helmet.GetterSetters;

public class HelmetDetails {

    private String Hid, Name, Type, user, Email;


    public HelmetDetails(String Hid, String Name, String Type, String user, String email) {
        this.Name = Name;
        this.Hid = Hid;
        this.Type = Type;
        this.user = user;
        this.Email = email;
    }

    public String getHid() {
        return Hid;
    }

    public String getName() {
        return Name;
    }

    public String getType() {
        return Type;
    }

    public String getUser() {
        return user;
    }

    public void setHid(String hid) {
        Hid = hid;
    }

    public String getEmail() { return Email; }

    public void setEmail(String email) { Email = email; }

    public void setName(String name) {
        Name = name;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
