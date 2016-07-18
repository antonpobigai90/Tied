package com.tied.android.tiedapp.customs.model;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ScheduleTimeModel {

    private String id;
    private String title;
    private String start_time;
    private String end_time;

    public ScheduleTimeModel(String id, String title, String start_time, String end_time) {
        this.id = id;
        this.title = title;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return "ScheduleTimeModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                '}';
    }
}
