package com.example.parapo.login.sign_up;

public class TravelersData {
    public String user_id, full_name, gender, birthday;
    public boolean is_online;
    public double latitude, longitude;

    public TravelersData(){

    }

    public TravelersData(String user_id, String full_name, String birthday, String gender, double latitude, double longitude, boolean is_online) {
        this.user_id = user_id;
        this.full_name = full_name;
        this.gender = gender;
        this.birthday = birthday;
        this.latitude = latitude;
        this.longitude = longitude;
        this.is_online = is_online;
    }
}
