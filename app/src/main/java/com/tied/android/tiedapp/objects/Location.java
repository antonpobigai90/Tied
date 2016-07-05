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
    private Coordinate coordinate;

    public Location(String city, String zip, String state, String country, String street, Coordinate coordinate) {
        this.city = city;
        this.zip = zip;
        this.state = state;
        this.country = country;
        this.street = street;
        this.coordinate = coordinate;
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


    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return "Location{" +
                "city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", street='" + street + '\'' +
                ", coordinate=" + coordinate +
                '}';
    }
}
