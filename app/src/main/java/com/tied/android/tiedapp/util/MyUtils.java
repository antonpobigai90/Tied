package com.tied.android.tiedapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Distance;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Femi on 7/19/2016.
 */
public abstract class MyUtils {
    public static class Picasso {
        public static void displayImage(final String imageUrl, final ImageView imageView) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                com.squareup.picasso.Picasso.with(MainApplication.getInstance().getApplicationContext())
                        .load(imageUrl)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
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
            } else {
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

    public static boolean locationValidation(com.tied.android.tiedapp.objects.Location location){
        if(location == null){
            return false;
        }
        if(location.getStreet().isEmpty()) {
            MyUtils.showToast("You must provide a street address");
            return false;
        }
        if(location.getCity().isEmpty()) {
            MyUtils.showToast("You must provide a city");
            return false;
        }
        if(location.getState().isEmpty()) {
            MyUtils.showToast("You must provide a state address");
            return false;
        }
        if(location.getZip().isEmpty()) {
            MyUtils.showToast("You must provide a zip code");
            return false;
        }
        return true;
    }

    /**
     * startActivity starts a new activity without a bundle
     *
     * @param a           Activity :parent activity
     * @param newActivity Class :new activity class
     */
    public static void startActivity(Context a, Class newActivity) {
        startActivity(a, newActivity, null);
    }

    /**
     * startActivity: starts a new activity
     *
     * @param a           Activity  :parent activity
     * @param newActivity Class :activity to be started
     * @param b           Bundle :bundle data to be passed
     */
    public static void startActivity(Context a, Class newActivity, Bundle b) {
        Intent i = new Intent(a, newActivity);

        if (b == null) {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            a.startActivity(i);
        } else {
            Logger.write("bundle added");
            i.putExtras(b);
            a.startActivity(i
            );
        }
    }

    public static void initAvatar(Bundle bundle, ImageView imageView) {
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            User user = gson.fromJson(user_json, User.class);
            if (user != null) {
                MyUtils.Picasso.displayImage(user.getAvatar(), imageView);
            }
        }
    }

    /**
     * startActivity: starts a new activity
     *
     * @param a           Activity  :parent activity
     * @param newActivity Class :activity to be started
     * @param requestCode int:bundle data to be passed
     */
    public static void startRequestActivity(Activity a, Class newActivity, int requestCode) {
        Intent i = new Intent(a, newActivity);


        // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        a.startActivityForResult(i, requestCode);

    }

    public static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(MainApplication.getInstance().getApplicationContext());
    }

    public static Coordinate getCurrentLocation() {
        SharedPreferences sp = getSharedPreferences();
        double lat = Double.parseDouble(sp.getString(Constants.LATITUDE, "0.0"));
        double lon = Double.parseDouble(sp.getString(Constants.LONGITUDE, "0.0"));
        return new Coordinate(lat, lon);
    }

    public static void setCurrentLocation(Coordinate coordinate) {
        SharedPreferences.Editor e = getSharedPreferences().edit();
        e.putString(Constants.LATITUDE, "" + coordinate.getLat());
        e.putString(Constants.LONGITUDE, "" + coordinate.getLon());
        e.apply();
    }

    public static String getPreferredDistanceUnit() {
        SharedPreferences sp = getSharedPreferences();

        return sp.getString(Constants.DISTANCE_UNIT, Distance.UNIT_MILES);
    }

    public static void setPreferredDistanceUnit(String unit) {
        SharedPreferences.Editor e = getSharedPreferences().edit();
        e.putString(Constants.DISTANCE_UNIT, unit);
        e.apply();
    }

    public static void setLastTimeAppRan(long date) {
        SharedPreferences.Editor e = getSharedPreferences().edit();
        e.putLong(Constants.LAST_TIME_APP_RAN, date);
        e.apply();
    }

    public static long getLastTimeAppRan() {
        SharedPreferences sp = getSharedPreferences();

        return sp.getLong(Constants.LAST_TIME_APP_RAN, 0);
    }

    public static User getUserFromBundle(Bundle bundle) {
        User user = null;
        Gson gson = new Gson();
        try {
            if (bundle != null) {

                String user_json = bundle.getString(Constants.USER_DATA);
                user = gson.fromJson(user_json, User.class);

            } else {
                Logger.write(getSharedPreferences().getString(Constants.CURRENT_USER, null));
                user = getUserLoggedIn();
            }

        } catch (Exception e) {
            Logger.write(e);
        }
        return user;
    }

    public static User getUserLoggedIn() {
        Gson gson = new Gson();
        return gson.fromJson(getSharedPreferences().getString(Constants.CURRENT_USER, null), User.class);
    }

    public static void showToast(String message) {
        Toast.makeText(MainApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    public static void getLatLon(String address, final HTTPConnection.AjaxCallback cb) {
        try {
            address = URLEncoder.encode(address, "UTF-8");
        } catch (Exception e) {

        }
        new HTTPConnection(new HTTPConnection.AjaxCallback() {
            @Override
            public void run(int code, String response) {
                // Logger.write(code+": "+response);
                if (code == 0) {//network error
                    cb.run(0, "");
                } else {
                    try {
                        JSONObject jo = new JSONObject(response);
                        JSONArray ja = new JSONArray(jo.getString("results"));
                        int lent = ja.length();

                        for (int k = 0; k < lent; k++) {
                            JSONObject addrJO = new JSONObject(ja.get(k).toString());
                            JSONObject coordCompObj = new JSONObject(addrJO.getString("geometry"));
                            JSONObject locObj = new JSONObject(coordCompObj.getString("location"));
                            Coordinate coordinate = new Coordinate(locObj.getDouble("lat"), locObj.getDouble("lng"));
                            cb.run(200, coordinate.toJSONString());
                            break;
                        }
                    } catch (JSONException jje) {
                        Logger.write(jje);
                        cb.run(0, "");
                    }
                }

            }
        }).load(Constants.GOOGLE_REVERSE_GEOCODING_URL + "&address=" + address);
    }

    public static _Meta getMeta(JSONObject response) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(response.getString("_meta"), _Meta.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isAuthFailed(JSONObject response) {
        try {
            return !response.getBoolean("success");
        } catch (Exception e) {
            return false;
        }
    }

    public static String moneyFormat(float amount) {
        return NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(amount);
    }

    public static String moneyFormat(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(amount);
    }

    public static String getDistance(Coordinate start, Coordinate stop) {
        try {
            Location mallLoc = new Location("");
            mallLoc.setLatitude(start.getLat());
            mallLoc.setLongitude(start.getLon());

            Location userLoc = new Location("");
            userLoc.setLatitude(stop.getLat());
            userLoc.setLongitude(stop.getLon());

            float distance = 0.621371f * (mallLoc.distanceTo(userLoc) / 1000);
            return String.format(Locale.getDefault(), "%.0f", distance)
                    + " km";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return "Unknown";
        }
    }

}
