package com.example.parapo.login.sign_up;

public class TravellerData {
    public String fullName, email, birthdate, password;

    public TravellerData(){

    }

    public TravellerData(String fullName, String email, String birthdate, String password){
        this.fullName = fullName;
        this.email = email;
        this.birthdate = birthdate;
        this.password = password;
    }
}
