package com.tied.android.tiedapp.objects;

import java.io.Serializable;

/**
 * Created by Emmanuel on 4/23/2016.
 */
public class Location implements Serializable {
    private String city;
    private String zip;
    private String state;
    private String country;
    private String street;
    private double longitude;
    private double latitude;

    public Location(String city, String zip, String state, String country, String street, double longitude, double latitude) {
        this.city = city;
        this.zip = zip;
        this.state = state;
        this.country = country;
        this.street = street;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location(String city, String zip, String state, String street) {
        this.city = city;
        this.zip = zip;
        this.state = state;
        this.street = street;
    }

    public String getLocationAddress(){
        return street+", " +city+", "+state +", "+zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", street='" + street + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
