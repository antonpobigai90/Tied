package com.tied.android.tiedapp.objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.tied.android.tiedapp.customs.Constants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Femi on 7/22/2016.
 */
public class Line implements Serializable {

    public static final String TAG = Line.class.getSimpleName();

    private String id;
    private String user_id;
    private String description;
    private Location address;
    private String name;

    private int dis_from;
    private ArrayList _score;


    public Line() {
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static void lineCreated(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean(Constants.LINE_CREATED, true);
        prefsEditor.apply();
    }

    public static boolean isLineCreated(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getBoolean(Constants.LINE_CREATED, false);
    }

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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Location getAddress() {
        return address;
    }

    public void setAddress(Location address) {
        this.address = address;
    }


    public ArrayList get_score() {
        return _score;
    }

    public void set_score(ArrayList _score) {
        this._score = _score;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}


