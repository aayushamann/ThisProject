package com.saluchen.thisproject.Database;

public class Response {

    public String last_location;
    public String status;
    public String delay;

    public Response() {
    }

    public Response(String last_location, String status, String delay) {

        this.last_location = last_location;
        this.status = status;
        this.delay = delay;
    }
}
