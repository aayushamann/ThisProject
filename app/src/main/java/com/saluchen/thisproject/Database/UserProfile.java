package com.saluchen.thisproject.Database;

public class UserProfile {

    public String name;
    public String phone;
    public String request_count;
    public String latitude;
    public String longitude;

    public UserProfile() {
    }

    public UserProfile(String name, String phone, String request_count, String latitude, String longitude) {
        this.name = name;
        this.phone = phone;
        this.request_count = request_count;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
