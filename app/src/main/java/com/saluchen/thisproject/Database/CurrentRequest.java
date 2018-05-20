package com.saluchen.thisproject.Database;

public class CurrentRequest {

    public String latitude;
    public String longitude;
    public String request_title;
    public String details;
    public String datetime;
    public String accept_id;

    public CurrentRequest (String latitude, String longitude, String request_title, String details,
                           String datetime, String accept_id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.request_title = request_title;
        this.details = details;
        this.datetime = datetime;
        this.accept_id = accept_id;
    }
}
