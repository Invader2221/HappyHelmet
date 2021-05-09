package com.example.happy_helmet.GetterSetters;

public class Users{

    private String Name, password, admin, stuId, phone, email;

    public Users() {
    }

    public Users(String name, String password, String admin, String stuId, String phone, String email) {
        this.Name = name;
        this.password = password;
        this.admin = admin;
        this.stuId = stuId;
        this.phone = phone;
        this.email = email;
    }

    public String getStuid() {
        return stuId;
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
