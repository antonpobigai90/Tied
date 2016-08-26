package com.tied.android.tiedapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.MyStringAsyncTask;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Distance;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

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
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
    public static final int MESSAGE_TOAST=0, ERROR_TOAST=1;
    public static void showAlert(Activity  activity, String message) {
        showAlert(activity, message, MESSAGE_TOAST);
    }
    public static MyStringAsyncTask animateTask=null;
    public static void showAlert(Activity activity, String message, int type) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        boolean isFirstTime=false;
        View layout= activity.getWindow().getDecorView().getRootView();
        View view=layout.findViewById(R.id.custom_alert);
        if(view==null) {
            isFirstTime=true;
            LayoutInflater inflater = LayoutInflater.from(MainApplication.getInstance().getApplicationContext());
            view=inflater.inflate(R.layout.custom_toast, null);
            //view.setVisibility(View.INVISIBLE);
            ((ViewGroup)layout).addView(view, params);
            //view.setVisibility(View.GONE);
           // view.setTranslationY(-80.0f);
            //view.setVisibility(View.GONE);
        }
        final AlertLayout v=(AlertLayout)view;
       // v.setVisibility(View.VISIBLE);

        TextView msgTV=(TextView)v.findViewById(R.id.txt_alert);
        msgTV.setText(message);

        if(animateTask!=null) {
            animateTask.cancel(true);
        }

       // v.setTranslationY(100.0f);

        if(isFirstTime) {
            Logger.write("its first time");
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    v.setVisibility(View.VISIBLE);
                }
            }, 300);
        }else{
            Logger.write("its not first time");
            v.setVisibility(View.VISIBLE);
        }
       // v.animate().translationY(100).setDuration(1000).start();

        animateTask=new MyStringAsyncTask() {
            boolean cancelled=false;

            public void setCancelled(boolean cancelled) {
                this.cancelled = cancelled;
            }
            @Override
            protected String doInBackground(Void... params) {
                while (true) {
                    try {
                        Thread.sleep(3500);
                    } catch (Exception e) {

                    }
                    break;
                }
              return super.doInBackground(params);
            }

            @Override
            protected void onPostExecute(String s) {
                if(!isCancelled()) {
                    Logger.write("hidinnggg");
                    v.setVisibility(View.GONE);
                }
                animateTask=null;
            }
        };
        animateTask.execute();

        Logger.write(message);
    }

    public static void showAddressDialog(final Activity context, String title, final com.tied.android.tiedapp.objects.Location currentLocation, final DialogClickListener okayClicked) {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.address_dialog_activity);
        //dialog.setTitle(title.toUpperCase());
        //dialog.setFeatureDrawableAlpha(Backg);

        final EditText streetET, cityET,zipET, territoryET;
        final Spinner stateSpinner;
        streetET=(EditText)dialog.findViewById(R.id.street);
        cityET=(EditText)dialog.findViewById(R.id.city);
        zipET=(EditText)dialog.findViewById(R.id.zip);
        stateSpinner = (Spinner) dialog.findViewById(R.id.state);
        List<String> states = MyUtils.States.asArrayList();
        states.add(0, "Select...");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.my_spinner_item, states);
        adapter.setDropDownViewResource(R.layout.my_spinner_dropdown);
        stateSpinner.setAdapter(adapter);
        //stateSpinner.setSelection(adapter.getPosition("TX"));

        if(currentLocation!=null) {
            streetET.setText(currentLocation.getStreet());
            cityET.setText(currentLocation.getCity());
            stateSpinner.setSelection(adapter.getPosition(currentLocation.getState()));
            zipET.setText(currentLocation.getZip());
        }

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.txt_title);
        text.setText(title.toUpperCase());

       View.OnClickListener cancelClicked=new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            };

        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(cancelClicked);
        View.OnClickListener okayButClicked=new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String street = streetET.getText().toString().trim();
                    String city = cityET.getText().toString().trim();
                    String state = stateSpinner.getSelectedItem().toString().trim();
                    String zip=zipET.getText().toString().trim();

                    if(street.isEmpty()) {
                        MyUtils.showToast( "You must provide a street address");
                        return;
                    }
                    if(city.isEmpty()) {
                        MyUtils.showToast("You must enter a city");
                        return;
                    }
                    if(state.isEmpty() || state.toLowerCase().contains("select")) {
                        MyUtils.showToast("You must provide a state address");
                        return;
                    }
                    if(zip.isEmpty()) {
                        MyUtils.showToast( "You must provide a zip code");
                        return;
                    }

                    final com.tied.android.tiedapp.objects.Location location = new com.tied.android.tiedapp.objects.Location(city, zip, state,  street);
                    location.setCountry("US");
                    okayClicked.onClick(location);
                    dialog.dismiss();
                }
            };

        Button okButton = (Button) dialog.findViewById(R.id.ok_button);
        // if button is clicked, close the custom dialog
        okButton.setOnClickListener(okayButClicked);

        dialog.show();
    }
    public interface DialogClickListener {
        public void onClick(Object response);
    }

    public static void initClient(final Context context, User user, final ListAdapterListener listAdapterListener){

        ClientLocation clientLocation = new ClientLocation();
        clientLocation.setDistance("0km");
        Coordinate coordinate = MyUtils.getCurrentLocation();
        if( coordinate == null ){
            coordinate = user.getOffice_address().getCoordinate();
        }
        clientLocation.setCoordinate(coordinate);

        final ClientApi clientApi =  MainApplication.createService(ClientApi.class, user.getToken());
        Call<ClientRes> response = clientApi.getClientsByLocation(clientLocation);
        response.enqueue(new retrofit2.Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if ( context == null ) return;
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
                if(clientRes.isAuthFailed()){
                    User.LogOut(context);
                }
                else if(clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200){
                    ArrayList<Client> clients = clientRes.getClients();
                    if(clients.size() > 0){
                        MainApplication.clientsList = clients;
                        if (listAdapterListener != null){
                            listAdapterListener.listInit(clients);
                        }
                    }
                }else{
                    Log.d("Error onResponse", clientRes.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }
        });
    }

}
