package com.saluchen.thisproject.Database;

public class CurrentRequest {

    public String latitude;
    public String longitude;
    public String requestTitle;
    public String details;
    public String dateTime;
    public String accept_id;

    public CurrentRequest() {}

    public CurrentRequest (String latitude, String longitude, String requestTitle, String details,
                           String dateTime, String accept_id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.requestTitle = requestTitle;
        this.details = details;
        this.dateTime = dateTime;
        this.accept_id = accept_id;
    }

}
