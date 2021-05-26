package com.example.happy_helmet.GetterSetters;

public class Users{

    private String Name, password, admin, stuid, phone, email;

    public Users() {
    }

    public Users(String name, String stuid, String phone, String email) {
        this.Name = name;
        this.stuid = stuid;
        this.phone = phone;
        this.email = email;
    }

    public String getStuid() {
        return stuid;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return Name;
    }
    public void setName(String name) { Name = name;
    }

    public String getPassword() {
        return password;
    }
    public String getAdmin() {
        return admin;
    }
}
