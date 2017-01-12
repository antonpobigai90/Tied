package com.tied.android.tiedapp.objects.schedule;

import com.google.gson.Gson;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.sales.RevenueFilter;

import java.io.Serializable;

/**
 * Created by femi on 11/28/2016.
 */
public class ScheduleFilter implements Serializable {

    private TimeRange time_range;
    private DateRange date_range;
    private String client_id, unit, order_by, order;
    int status, distance;
    Coordinate coordinate;
    public ScheduleFilter() {
    }

    public ScheduleFilter(TimeRange time_range, DateRange date_range) {
        this.time_range = time_range;
        this.date_range = date_range;
    }

    public TimeRange getTime_range() {
        return time_range;
    }

    public void setTime_range(TimeRange time_range) {
        this.time_range = time_range;
    }

    public DateRange getDate_range() {
        return date_range;
    }

    public void setDate_range(DateRange date_range) {
        this.date_range = date_range;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrder_by() {
        return order_by;
    }

    public void setOrder_by(String order_by) {
        this.order_by = order_by;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    @Override
    public String toString() {
        return "ScheduleDate{" +
                "time_range=" + time_range +
                ", date_range=" + date_range +
                '}';
    }
    public String toJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static ScheduleFilter fromJSONString(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, ScheduleFilter.class);
    }
}
