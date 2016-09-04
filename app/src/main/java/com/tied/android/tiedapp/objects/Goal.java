package com.tied.android.tiedapp.objects;

import java.io.Serializable;

/**
 * Created by Emmanuel on 9/4/2016.
 */
public class Goal implements Serializable {


    private String id;
    private String object_id;
    private String user_id;
    private String title;
    private String description;
    private String type;
    private String target = "00.00";
    private String value;
    private String date;
    private String created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getProgress(){
        return target +" of "+value +" Goals";
    }

    public String getExpirationDate(){
        return date;
    }

    public String getClientLinesCountString(){
        return "Expires in 4 days";
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "Goal{" +
                "id='" + id + '\'' +
                ", object_id='" + object_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", target='" + target + '\'' +
                ", value='" + value + '\'' +
                ", date='" + date + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}
