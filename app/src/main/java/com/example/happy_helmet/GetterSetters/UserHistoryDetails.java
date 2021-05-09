package com.example.happy_helmet.GetterSetters;

public class UserHistoryDetails {

    private String HelmetId;
    private String HelmetIdSmall;
    private String CheckIn;
    private String CkeckOut;


    public UserHistoryDetails(String helmetID, String HelmetIdSmall, String checkIn, String checkOut) {
        this.HelmetId = helmetID;
        this.HelmetIdSmall = HelmetIdSmall;
        this.CheckIn = checkIn;
        this.CkeckOut = checkOut;
    }

    public String getHelmetHistoryId() {
        return HelmetId;
    }
    public String getHelmetIdSmall() {
        return HelmetIdSmall;
    }

    public String getHelmetCheckIn() { return CheckIn; }

    public String getHelmetCkeckOut() {
        return CkeckOut;
    }

}

