package com.tied.android.tiedapp.objects.visit;

import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Territory;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class VisitFilter implements Serializable {
    private String client_id;
    private String schedule_id;
    private int month;
    private int year;
    private String date;
    private int distance;
    private String unit;

    public VisitFilter() {
    }

    public VisitFilter(String client_id, String schedule_id, int month, int year, String date, int distance, String unit) {
        this.client_id = client_id;
        this.schedule_id = schedule_id;
        this.month = month;
        this.year = year;
        this.date = date;
        this.distance = distance;
        this.unit = unit;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "VisitFilter{" +
                "client_id='" + client_id + '\'' +
                ", schedule_id='" + schedule_id + '\'' +
                ", month=" + month +
                ", year=" + year +
                ", date='" + date + '\'' +
                ", distance=" + distance +
                ", unit='" + unit + '\'' +
                '}';
    }
}
