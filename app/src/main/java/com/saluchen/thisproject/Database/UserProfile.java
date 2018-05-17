package com.saluchen.thisproject.Database;

public class UserProfile {

    public String name;
    public String phone;
    public String requestCount;

    public UserProfile() {
    }

    public UserProfile(String name, String phone, String requestCount) {
        this.name = name;
        this.phone = phone;
        this.requestCount = requestCount;
    }
}
