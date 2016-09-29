package com.tied.android.tiedapp.objects;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by femi on 9/28/2016.
 */
public class RevenueFilter  implements Serializable {
    String start_date, end_date, sort;
    int quarter;

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public String toJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static RevenueFilter fromString(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, RevenueFilter.class);
    }
}
