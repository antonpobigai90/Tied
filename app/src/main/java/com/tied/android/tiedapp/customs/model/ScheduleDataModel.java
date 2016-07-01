package com.tied.android.tiedapp.customs.model;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ScheduleDataModel {

    private String day;
    private String week_day;
    private ArrayList<ScheduleTimeModel> scheduleTimeModel;
    private String temperature;
    private String weather;

    public ScheduleDataModel(String day, String week_day, String temperature, String weather, ArrayList<ScheduleTimeModel> scheduleTimeModel) {
        this.day = day;
        this.week_day = week_day;
        this.temperature = temperature;
        this.weather = weather;
        this.scheduleTimeModel = scheduleTimeModel;
    }

    public ScheduleDataModel() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek_day() {
        return week_day;
    }

    public void setWeek_day(String week_day) {
        this.week_day = week_day;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public ArrayList<ScheduleTimeModel> getScheduleTimeModel() {
        return scheduleTimeModel;
    }

    public void setScheduleTimeModel(ArrayList<ScheduleTimeModel> scheduleTimeModel) {
        this.scheduleTimeModel = scheduleTimeModel;
    }

    @Override
    public String toString() {
        return "ScheduleDataModel{" +
                "day='" + day + '\'' +
                ", week_day='" + week_day + '\'' +
                ", scheduleTimeModel=" + scheduleTimeModel +
                ", temperature='" + temperature + '\'' +
                ", weather='" + weather + '\'' +
                '}';
    }
}
