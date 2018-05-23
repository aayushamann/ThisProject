package com.saluchen.thisproject.models;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by User on 10/2/2017.
 */

public class PlaceInfo {

    private String name;
    private String address;
    private String Phonenumber;
    private String id;
    private Uri websiteUri;
    private LatLng LatLng;
    private float rating;
    private String attributions;

    public PlaceInfo(String name, String address, String Phonenumber, String id, Uri websiteUri,
                     LatLng LatLng, float rating, String attributions) {
        this.name = name;
        this.address = address;
        this.Phonenumber = Phonenumber;
        this.id = id;
        this.websiteUri = websiteUri;
        this.LatLng = LatLng;
        this.rating = rating;
        this.attributions = attributions;
    }

    public PlaceInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String Phonenumber) {
        this.Phonenumber = Phonenumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    public LatLng getLatLng() {
        return LatLng;
    }

    public void setLatLng(LatLng LatLng) {
        this.LatLng = LatLng;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", Phonenumber='" + Phonenumber + '\'' +
                ", id='" + id + '\'' +
                ", websiteUri=" + websiteUri +
                ", LatLng=" + LatLng +
                ", rating=" + rating +
                ", attributions='" + attributions + '\'' +
                '}';
    }
}