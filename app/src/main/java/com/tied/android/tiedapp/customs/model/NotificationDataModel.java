package com.tied.android.tiedapp.customs.model;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class NotificationDataModel {

    private String txt_time;
    private String txt_first;
    private String txt_user;
    private String txt_end;
    boolean bNew;

    public NotificationDataModel(String txt_time, String txt_first, String txt_end, String txt_user, Boolean bNew) {
        this.txt_time = txt_time;
        this.txt_first = txt_first;
        this.txt_end = txt_end;
        this.txt_user = txt_user;
        this.bNew = bNew;
    }

    public NotificationDataModel() {
    }

    public String getTxt_time() {
        return txt_time;
    }

    public void setTxt_time(String txt_time) {
        this.txt_time = txt_time;
    }

    public String getTxt_first() {
        return txt_first;
    }

    public void setTxt_first(String txt_first) {
        this.txt_first = txt_first;
    }

    public String getTxt_user() {
        return txt_user;
    }

    public void setTxt_user(String txt_user) {
        this.txt_user = txt_user;
    }

    public String getTxt_end() {
        return txt_end;
    }

    public void setTxt_end(String txt_end) {
        this.txt_end = txt_end;
    }

    public boolean isbNew() {
        return bNew;
    }

    public void setbNew(boolean bNew) {
        this.bNew = bNew;
    }
}
