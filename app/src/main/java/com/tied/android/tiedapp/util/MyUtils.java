package com.tied.android.tiedapp.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Distance;
import com.tied.android.tiedapp.objects.user.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import  com.tied.android.tiedapp.util.States;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Femi on 7/19/2016.
 */
public abstract class MyUtils {
    public static  class Picasso  {
        public static  void displayImage( final String imageUrl, final ImageView imageView) {
           if(imageUrl!=null && !imageUrl.isEmpty()) {
                com.squareup.picasso.Picasso.with(MainApplication.getInstance().getApplicationContext())
                        .load(imageUrl)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                //Try again online if cache failed
                                com.squareup.picasso.Picasso.with(MainApplication.getInstance().getApplicationContext())
                                        .load(imageUrl)
                                        .error(R.mipmap.ic_launcher)
                                        .into(imageView, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                                Logger.write("Picasso: Could not fetch image");
                                            }
                                        });
                            }
                        });
            }else {
                com.squareup.picasso.Picasso.with(MainApplication.getInstance().getApplicationContext())
                        .load("/")
                        .error(R.mipmap.ic_launcher)
                        .into(imageView, new Callback() {

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Logger.write("Picasso: Could not fetch image");
                            }

                        });
            }
        }
    }
    public static class States {
       public static List<String> asArrayList() {
           JSONArray states = com.tied.android.tiedapp.util.States.asJSONArray();
           int len = states.length();
           List<String> codes = new ArrayList<String>(len);
           for (int i = 0; i < len; i++) {
               try {
                   codes.add(states.getJSONObject(i).getString("code"));
               } catch (JSONException je) {

               }
           }
           return codes;
       }
    }
    /**
     * startActivity starts a new activity without a bundle
     * @param a Activity :parent activity
     * @param newActivity Class :new activity class
     */
    public static void startActivity(Context a, Class newActivity) {
        startActivity(a, newActivity, null);
    }

    /**
     * startActivity: starts a new activity
     * @param a Activity  :parent activity
     * @param newActivity Class :activity to be started
     * @param b Bundle :bundle data to be passed
     */
    public static void startActivity(Context a, Class newActivity, Bundle b) {
        Intent i = new Intent(a, newActivity);

        if (b == null) {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            a.startActivity(i);
        }
        else{
            Logger.write("bundle added");
            i.putExtras(b);
            a.startActivity(i
            );
        }
    }
    public static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(MainApplication.getInstance().getApplicationContext());
    }
    public static Coordinate getCurrentLocation() {
        SharedPreferences sp=getSharedPreferences();
        double lat = Double.parseDouble(sp.getString(Constants.LATITUDE,"0.0"));
        double lon = Double.parseDouble(sp.getString(Constants.LONGITUDE,"0.0"));
        return new Coordinate(lat, lon);
    }
    public static void setCurrentLocation(Coordinate coordinate) {
        SharedPreferences.Editor e=getSharedPreferences().edit();
       e.putString(Constants.LATITUDE,""+coordinate.getLat());
        e.putString(Constants.LONGITUDE,""+coordinate.getLon());
        e.apply();
    }
    public static String getPreferredDistanceUnit() {
        SharedPreferences sp=getSharedPreferences();

        return sp.getString(Constants.DISTANCE_UNIT, Distance.UNIT_MILES);
    }
    public static void setPreferredDistanceUnit(String unit) {
        SharedPreferences.Editor e=getSharedPreferences().edit();
        e.putString(Constants.DISTANCE_UNIT,unit);

        e.apply();
    }

    public static User getUserFromBundle(Bundle bundle) {
        User user=null;
        Gson gson = new Gson();
        try {
            if (bundle != null) {

                String user_json = bundle.getString(Constants.USER_DATA);
                user = gson.fromJson(user_json, User.class);

            } else {
                Logger.write(getSharedPreferences().getString(Constants.CURRENT_USER, null));
                user = gson.fromJson(getSharedPreferences().getString(Constants.CURRENT_USER, null), User.class);
            }

        }catch (Exception e) {
                Logger.write(e);
        }
        return user;
    }
}
