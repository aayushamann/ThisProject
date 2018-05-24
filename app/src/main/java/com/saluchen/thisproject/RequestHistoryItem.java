package com.saluchen.thisproject;

public class RequestHistoryItem {

    private String itemName;
    private String itemDetails;
    private String expectedDatetime;
    private String deliveryLocation;
    private String status;

    public RequestHistoryItem() {
    }

    ;

    public RequestHistoryItem(String itemName, String itemDetails, String expectedDatetime,
                              String deliveryLocation, String status) {
        this.itemName = itemName;
        this.itemDetails = itemDetails;
        this.expectedDatetime = expectedDatetime;
        this.deliveryLocation = deliveryLocation;
        this.status = status;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public String getExpectedDatetime() {
        return expectedDatetime;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemDetails(String itemDetails) {
        this.itemDetails = itemDetails;
    }

    public void setExpectedDatetime(String expectedDatetime) {
        this.expectedDatetime = expectedDatetime;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
