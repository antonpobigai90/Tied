package com.tied.android.tiedapp.objects;

import java.io.Serializable;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class Schedule implements Serializable{

    private String id;
    private int user_id;
    private String client_id;
    private String title;
    private int reminder;
    private boolean visited;
    private String date;
    private String start_time;
    private String end_time;
    private Location location;

    public Schedule() {
    }

    public Schedule(String id, int user_id, String client_id, String title, int reminder,
                    boolean visited, String date, String start_time, String end_time, Location location) {
        this.id = id;
        this.user_id = user_id;
        this.client_id = client_id;
        this.title = title;
        this.reminder = reminder;
        this.visited = visited;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    @Override
    public String toString() {
        return "Schedule{" +
                "id='" + id + '\'' +
                ", user_id=" + user_id +
                ", client_id='" + client_id + '\'' +
                ", title='" + title + '\'' +
                ", reminder=" + reminder +
                ", visited=" + visited +
                ", date='" + date + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", location=" + location +
                '}';
    }
}
