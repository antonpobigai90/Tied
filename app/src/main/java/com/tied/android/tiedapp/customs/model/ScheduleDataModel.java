package com.tied.android.tiedapp.customs.model;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ScheduleDataModel {

    private String day;
    private ScheduleTimeModel scheduleTimeModel;
    private String temperature;
    private String weather;

    public ScheduleDataModel(String day, ScheduleTimeModel scheduleTimeModel, String weather, String temperature) {
        this.day = day;
        this.scheduleTimeModel = scheduleTimeModel;
        this.weather = weather;
        this.temperature = temperature;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ScheduleTimeModel getScheduleTimeModel() {
        return scheduleTimeModel;
    }

    public void setScheduleTimeModel(ScheduleTimeModel scheduleTimeModel) {
        this.scheduleTimeModel = scheduleTimeModel;
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

    @Override
    public String toString() {
        return "ScheduleDataModel{" +
                "day='" + day + '\'' +
                ", scheduleTimeModel=" + scheduleTimeModel +
                ", temperature='" + temperature + '\'' +
                ", weather='" + weather + '\'' +
                '}';
    }
}
