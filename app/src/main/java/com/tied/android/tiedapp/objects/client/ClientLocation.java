package com.tied.android.tiedapp.objects.client;

import com.tied.android.tiedapp.objects.Coordinate;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class ClientLocation {
    private String distance;
    private Coordinate location;

    public ClientLocation(String distance, Coordinate location) {
        this.distance = distance;
        this.location = location;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "ClientLocation{" +
                "distance='" + distance + '\'' +
                ", location=" + location +
                '}';
    }
}
