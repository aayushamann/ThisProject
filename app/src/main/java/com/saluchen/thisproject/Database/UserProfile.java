package com.saluchen.thisproject.Database;

public class UserProfile {

    public String name;
    public String phone;
    public String request_count;

    public UserProfile() {
    }

    public UserProfile(String name, String phone, String request_count) {
        this.name = name;
        this.phone = phone;
        this.request_count = request_count;
    }
}
