package com.tied.android.tiedapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.MyStringAsyncTask;
import com.tied.android.tiedapp.customs.model.DataModel;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Distance;
import com.tied.android.tiedapp.objects.Goal;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.RevenueFilter;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.GoalApi;
import com.tied.android.tiedapp.retrofits.services.LineApi;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.retrofits.services.SignUpApi;
import com.tied.android.tiedapp.ui.activities.GeneralSelectObjectActivity;
import com.tied.android.tiedapp.ui.activities.SelectLineActivity;
import com.tied.android.tiedapp.ui.activities.client.NewClientActivity;
import com.tied.android.tiedapp.ui.activities.sales.ActivityAddSales;
import com.tied.android.tiedapp.ui.dialogs.DatePickerFragment;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.listeners.ListAdapterListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Femi on 7/19/2016.
 */
public abstract class MyUtils {
    public static User userLoggedIn=null;
    public static class Picasso {
        public static void displayImage(final String imageUrl, final ImageView imageView) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                com.squareup.picasso.Picasso.with(MainApplication.getInstance().getApplicationContext())
                        .load(imageUrl)
                       // .memoryPolicy(MemoryPolicy.C)
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
                                        .error(R.drawable.default_avatar)
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
                        .error(R.drawable.default_avatar)
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

    public static void setFocus(View view) {
        view.setFocusable(true);
        view.requestFocus();
        view.setFocusableInTouchMode(true);
    }

    public static void initiateClientSelector(Activity c,  Object selected, boolean isMultiple) {
        Intent i = new Intent(c, GeneralSelectObjectActivity.class);
        Bundle b=new Bundle();
        b.putInt(GeneralSelectObjectActivity.OBJECT_TYPE, GeneralSelectObjectActivity.SELECT_CLIENT_TYPE);
        b.putBoolean(GeneralSelectObjectActivity.IS_MULTIPLE, isMultiple);
        ArrayList<Object> selectedObjects=null;
        if(!(selected instanceof ArrayList)) {
            selectedObjects= new ArrayList<Object>(1);
            selectedObjects.add(selected);
        }else{
            selectedObjects=(ArrayList)selected;
        }
        if(selected!=null) b.putSerializable(GeneralSelectObjectActivity.SELECTED_OBJECTS, selectedObjects);
        i.putExtras(b);
        c.startActivityForResult(i, Constants.SELECT_CLIENT);

    }
    public static void initiateLineSelector(Activity c,  Object selected, boolean isMultiple) {
        Intent i = new Intent(c, SelectLineActivity.class);
        Bundle b=new Bundle();
        b.putInt(SelectLineActivity.OBJECT_TYPE, SelectLineActivity.SELECT_LINE_TYPE);
        b.putBoolean(SelectLineActivity.IS_MULTIPLE, isMultiple);
        ArrayList<Object> selectedObjects=null;
        if(!(selected instanceof ArrayList)) {
            selectedObjects= new ArrayList<Object>(1);
            selectedObjects.add(selected);
        }else{
            selectedObjects=(ArrayList)selected;
        }
        if(selected!=null) b.putSerializable(SelectLineActivity.SELECTED_OBJECTS, selectedObjects);
        i.putExtras(b);
        c.startActivityForResult(i, Constants.SELECT_LINE);

    }
    public static void initiateAddSales(Activity c,  Bundle bundle) {
        Intent i = new Intent(c, ActivityAddSales.class);
        i.putExtras(bundle);
        c.startActivityForResult(i, Constants.ADD_SALES);
    }
    public static void initAvatar(Bundle bundle, ImageView imageView) {
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            User user = gson.fromJson(user_json, User.class);
            if (user != null) {
                Picasso.displayImage(user.getAvatar(), imageView);
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
       MyUtils.startRequestActivity(a, newActivity, requestCode, null);

    }
    public static void startRequestActivity(Activity a, Class newActivity, int requestCode, Bundle bundle) {
        Intent i = new Intent(a, newActivity);

        if(bundle!=null) i.putExtras(bundle);
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
                if(user_json==null) {
                    user=(User)bundle.getSerializable(Constants.USER_DATA);
                    if(user==null)
                        user = getUserLoggedIn();
                }
                else user = gson.fromJson(user_json, User.class);

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
        if(userLoggedIn==null) {
            Gson gson = new Gson();
            userLoggedIn=gson.fromJson(getSharedPreferences().getString(Constants.CURRENT_USER, null), User.class);
        }
        return userLoggedIn;

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
            if(response.getInt("status")!=400)
                return !response.getBoolean("success");
            else return false;
        } catch (Exception e) {
            try{
             return response.getBoolean("authFailed");
            }catch (Exception ee) {
                return false;
            }
        }
    }

    public static String moneyFormat(float amount) {
        return NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(amount);
    }

    public static String moneyFormat(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(amount);
    }
    public static String getDistance(Coordinate start, Coordinate stop, boolean showUnit) {
        try {
            Location mallLoc = new Location("");
            mallLoc.setLatitude(start.getLat());
            mallLoc.setLongitude(start.getLon());

            Location userLoc = new Location("");
            userLoc.setLatitude(stop.getLat());
            userLoc.setLongitude(stop.getLon());

            float distance = 0.621371f * (mallLoc.distanceTo(userLoc) / 1000);
            return String.format(Locale.getDefault(), "%.0f", distance)
                    + (showUnit?" miles":"");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return "Unknown";
        }
    }
    public static String getDistance(Coordinate start, Coordinate stop) {
       return getDistance(start, stop, true);
    }
    public static final int MESSAGE_TOAST=0, ERROR_TOAST=1;
    public static void showAlert(Activity  activity, String message) {
        showAlert(activity, message, MESSAGE_TOAST);
    }
    public static void showMessageAlert(Activity  activity, String message) {
        showAlert(activity, message, MESSAGE_TOAST);
    }
    public static void showErrorAlert(Activity  activity, String message) {
        showAlert(activity, message, ERROR_TOAST);
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



    public static void showLinesRelevantInfoDialog(final Activity context,String title, final Line line, final MyDialogClickListener okayClicked) {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.line_relevant_info_dialog);
        //dialog.setTitle(title.toUpperCase());
        //dialog.setFeatureDrawableAlpha(Backg);

        final EditText websiteET, requestET,openingET;
        final Spinner reorderSpinner;
        websiteET=(EditText)dialog.findViewById(R.id.website);
        websiteET.setText(line.getWebsite()==null?"":line.getWebsite());

        requestET=(EditText)dialog.findViewById(R.id.special_request);
        requestET.setText(line.getRequest()==null?"":line.getRequest());

        openingET=(EditText)dialog.findViewById(R.id.openings);
        openingET.setText(line.getOpening()==null?"":line.getOpening());
        reorderSpinner=(Spinner)dialog.findViewById(R.id.reorders);


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
                String website = websiteET.getText().toString().trim();
                String request = requestET.getText().toString().trim();
                String opening = openingET.getText().toString().trim();
                String reorder="";
                if (reorderSpinner.getSelectedItem() != null) {
                   reorder = reorderSpinner.getSelectedItem().toString().trim();
                }
                line.setRelevantInfo(website, request, opening, reorder);
                okayClicked.onClick(line);
                dialog.dismiss();
            }
        };

        Button okButton = (Button) dialog.findViewById(R.id.ok_button);
        // if button is clicked, close the custom dialog
        okButton.setOnClickListener(okayButClicked);

        dialog.show();
    }


    public static void showAddressDialog(final Activity context, String title, final com.tied.android.tiedapp.objects.Location currentLocation, final MyDialogClickListener okayClicked) {
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
        List<String> states = States.asArrayList();
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
                    MyUtils.getLatLon(location.getLocationAddress(), new HTTPConnection.AjaxCallback() {
                        @Override
                        public void run(int code, String response) {
                            if(code!=200) {
                                MyUtils.showToast("Could not validate address!");
                            }else {
                                location.setCoordinate(Coordinate.fromJSONString(response));
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        okayClicked.onClick(location);
                                        dialog.dismiss();
                                    }
                                });

                            }
                            //dialog.dismiss();
                        }
                    });


                }
            };

        Button okButton = (Button) dialog.findViewById(R.id.ok_button);
        // if button is clicked, close the custom dialog
        okButton.setOnClickListener(okayButClicked);

        dialog.show();
    }
    public static void showEditTextDialog(final Activity context, String title, String initialValue, final MyDialogClickListener okayClicked) {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_text_dialog);
        //dialog.setTitle(title.toUpperCase());
        //dialog.setFeatureDrawableAlpha(Backg);

        final EditText textET=(EditText)dialog.findViewById(R.id.text_et);

        textET.setText(initialValue);
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
                okayClicked.onClick(textET.getText().toString());
                dialog.dismiss();
            }
        };

        Button okButton = (Button) dialog.findViewById(R.id.ok_button);
        // if button is clicked, close the custom dialog
        okButton.setOnClickListener(okayButClicked);

        dialog.show();
    }
    public interface MyDialogClickListener {
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
        Call<ClientRes> response = clientApi.getClientsByLocation(user.getId(), clientLocation);
        response.enqueue(new retrofit2.Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if ( context == null ) return;
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
                Logger.write(clientRes.toString());
                try {
                    if (clientRes.isAuthFailed()) {
                        // User.LogOut(context);
                    } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200) {
                        ArrayList<Client> clients = clientRes.getClients();

                        if (clients.size() > 0) {
                            MainApplication.clientsList = clients;
                            if (listAdapterListener != null) {
                                listAdapterListener.listInit(clients);
                            }
                        }
                    } else {
                        Logger.write("Error onResponse", clientRes.getMessage());
                    }
                }catch (Exception e) {
                    Logger.write(e);
                }
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }
        });
    }

    public static void initLines(final Context context, User user, final ListAdapterListener listAdapterListener){
        final LineApi lineApi =  MainApplication.createService(LineApi.class, user.getToken());
        Call<ResponseBody> response = lineApi.getUserLines( user.getId(), 1);
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(context);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code() == 200) {
                        ArrayList lines = (ArrayList) response.getDataAsList(Constants.LINES_lIST, Line.class);
                        if(lines.size() > 0){
                            if(MainApplication.linesList!=null) {
                                MainApplication.linesList.clear();
                                MainApplication.linesList.addAll(lines);
                            }else {
                                MainApplication.linesList=lines;
                            }
                            if (listAdapterListener != null){
                                listAdapterListener.listInit(lines);
                            }
                        }
                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }catch (IOException ioe) {
                    Logger.write(ioe);
                }
                catch (Exception jo) {
                    Logger.write(jo);
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }
        });
    }


    public static boolean currentUserIs(User user) {
        return MyUtils.getUserLoggedIn().getId().equals(user.getId());
    }
    public static void initGoals(final Context context, User user, final ListAdapterListener listAdapterListener){
        final GoalApi goalApi =  MainApplication.createService(GoalApi.class, user.getToken());
        Call<ResponseBody> response = goalApi.getUserGoals();
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(context);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code() == 200) {
                        ArrayList goals = (ArrayList) response.getDataAsList(Constants.GOAL_lIST, Goal.class);
                        if(goals.size() > 0){
                            MainApplication.goals = goals;
                            if (listAdapterListener != null){
                                listAdapterListener.listInit(goals);
                            }
                        }
                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }catch (IOException ioe) {
                    Logger.write(ioe);
                }
                catch (Exception jo) {
                    Logger.write(jo);
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }
        });
    }




    public static void initSchedules(final Context context, User user, final ListAdapterListener listAdapterListener){
        final ScheduleApi scheduleApi =  MainApplication.createService(ScheduleApi.class, user.getToken());
        Call<ResponseBody> response = scheduleApi.getSchedules();
        response.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                try {
                    GeneralResponse response = new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(context);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code() == 200) {
                        ArrayList schedule = (ArrayList) response.getDataAsList(Constants.SCHEDULE_LIST, Schedule.class);
                        if(schedule.size() > 0){
                            MainApplication.schedules = schedule;
                            if (listAdapterListener != null){
                                listAdapterListener.listInit(schedule);
                            }
                        }
                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }catch (IOException ioe) {
                    Logger.write(ioe);
                }
                catch (Exception jo) {
                    Logger.write(jo);
                }
                DialogUtils.closeProgress();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(" onFailure", t.toString());
            }
        });
    }
    public static String getTimeRange(Schedule schedule){
        String from = schedule.getTime_range().getStart_time();
        String to = schedule.getTime_range().getEnd_time();

        String range = getMeridianTime(from) +" - "+getMeridianTime(to);
        long diff = HelperMethods.getTimeDifference(from,to);
        int abs_difference = Math.abs((int)diff);

        if(abs_difference > 15){
            range = "All Day";
        }else if(abs_difference < 1){
            range = getMeridianTime(from) +" "+getMeridianString(from);
        }
        return range;
    }

    public static String getMeridianString(String hour){
        String[] hour_min = hour.split(":");
        int hour_int = Integer.parseInt(hour_min[0]);
        String str = "am";
        if(hour_int > 12){
            str = "pm";
        }
        return str;
    }
    public static String getMeridianTime(String hour){
        String[] hour_min = hour.split(":");
        int hour_int = Integer.parseInt(hour_min[0]);

        String new_hour = hour;
        if(hour_int > 12){
            new_hour = String.format("%02d", hour_int - 12) +":"+ hour_min[1];
        }
        return new_hour;
    }
    public static void showConnectionErrorToast(Activity a) {
        MyUtils.showToast(a.getString(R.string.connection_error));
    }
    public static String getWeekDay(Schedule schedule){
        int diff = (int) HelperMethods.getDateDifferenceWithToday(schedule.getDate());
        String result;
        if(diff < 7 && diff >= 0){
            switch (diff){
                case 0:
                    result = "Today";
                    break;
                case 1:
                    result = "Tomorrow";
                    break;
                default:
                    result = HelperMethods.getDayOfTheWeek(schedule.getDate());
            }
        }else{
            result = HelperMethods.getMonthOfTheYear(schedule.getDate());
        }
        return result;
    }
    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
    public static String distanceBetween(Coordinate c1, Coordinate c2) {
        int R = 6371; // km
        //R = 1; // miles
        double x = (c2.getLon() - c1.getLon()) * Math.cos((c1.getLat() + c2.getLat()) / 2);
        double y = (c2.getLat() - c1.getLat());
       return Math.sqrt(x * x + y * y) * R +" miles";
    }
    public static String toNth(Object num) {
        int dayOfYear;
        String day=""+num;
        if(num instanceof Integer) dayOfYear=(Integer)num;
        else dayOfYear=Integer.parseInt((String)num);
        switch (dayOfYear > 20 ? (dayOfYear % 10) : dayOfYear) {
            case 1:
             day= dayOfYear + "st";
            break;
            case 2:
                day= dayOfYear + "nd";
            break;
            case 3:  day=dayOfYear + "rd";
            break;
            default:  day= dayOfYear + "th";
            break;
        }
        return day;
    }

    public static boolean isSameDay(String day1, String day2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = sdf.parse(day1);
            Date date2 = sdf.parse(day2);
            return date1.compareTo(date2) == 0;
        } catch (ParseException e) {
            return false;
        }
    }

    public static void initIndustryList(){
        Call<List<DataModel>> response = MainApplication.getInstance().getRetrofit().create(SignUpApi.class).getIndustries();
        response.enqueue(new retrofit2.Callback<List<DataModel>>() {
            @Override
            public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> listResponse) {
                if (MainApplication.getInstance().getApplicationContext() == null) return;
                //DialogUtils.closeProgress();
                try {
                    List<DataModel> dataModelList = listResponse.body();
                    JSONArray ja= new JSONArray();
                    for(DataModel data:dataModelList) {
                        ja.put(data.toJSONString());
                    }
                    SharedPreferences.Editor e=MyUtils.getSharedPreferences().edit();
                    e.putString(Constants.INDUSTRIES, ja.toString());
                    e.apply();

                }catch (Exception e) {

                }
                //Log.d(TAG + " onResponse", dataModelList.toString());
            }

            @Override
            public void onFailure(Call<List<DataModel>> call, Throwable t) {
                Logger.write(" onFailure", t.toString());
               // DialogUtils.closeProgress();
            }
        });
    }
    public static ArrayList<DataModel> getIndustriesAsList() {
        SharedPreferences sp=MyUtils.getSharedPreferences();
        String ind=sp.getString(Constants.INDUSTRIES, null );
        if(ind==null) {
            initIndustryList();
        }
        ArrayList<DataModel> dataModels=new ArrayList<>(0);
        try{
            JSONArray ja=new JSONArray(ind);
            int len=ja.length();
             dataModels=new ArrayList<DataModel>(len);
            Gson gson=new Gson();

            for(int i=0; i<len; i++) {
                dataModels.add(gson.fromJson(ja.getString(i), DataModel.class));
            }
        }catch (Exception e) {

        }
        return dataModels;
    }


    public static Pair<String,String> getWeekRange(int year, int week_no) {

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week_no);
        Date monday = cal.getTime();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week_no);
        Date sunday = cal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return new Pair<String,String>(sdf.format(monday), sdf.format(sunday));
    }
    public static String getClientName(Client client) {
        return (client.getCompany()==null || client.getCompany().isEmpty())?client.getFull_name():client.getCompany();
    }


    public static android.util.Pair<String, String> getDateRange() {
        Date begining, end;

        {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            begining = calendar.getTime();
        }

        {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndofDay(calendar);
            end = calendar.getTime();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return new android.util.Pair<String,String>(sdf.format(begining), sdf.format(end));
    }

    public static Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    public static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }
    public static String formatDate(Date date) {
        Calendar c= Calendar.getInstance();
        c.setTime(date);
        return toNth(c.get(Calendar.DAY_OF_MONTH))+" "+ DatePickerFragment.MONTHS_LIST[c.get(Calendar.MONTH)+1]+", "+c.get(Calendar.YEAR);
    }

    public static Date parseDate(String pattern, String date) throws Exception {
        return new SimpleDateFormat(pattern).parse(date);
    }
    public static Date parseDate( String date) throws Exception {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    public static class MapObject  {
        public static  Map<String, Object> create(String jsonString) {
            return  new Gson().fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {}.getType());
        }
    }



    public static void showNewLineDialog(final Activity context, String title, final MyDialogClickListener okayClicked) {
        // custom dialog
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_line_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText name, description;

        final Spinner stateSpinner;
        name=(EditText)dialog.findViewById(R.id.street);
//        final InputMethodManager inputMethodManager = (InputMethodManager) context
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);

        description=(EditText)dialog.findViewById(R.id.description);
        //stateSpinner.setSelection(adapter.getPosition("TX"));

        TextView text = (TextView) dialog.findViewById(R.id.txt_title);
        text.setText(title.toUpperCase());

        View.OnClickListener cancelClicked=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        };

        TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(cancelClicked);

        View.OnClickListener okayButClicked=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                MyUtils.startActivity(context, NewClientActivity.class);
            }
        };

        TextView txt_create = (TextView) dialog.findViewById(R.id.txt_add);
        // if button is clicked, close the custom dialog
        txt_create.setOnClickListener(okayButClicked);

        dialog.show();
    }

    public static void showLineNewRevenueDialog(final Activity context, String title, final MyDialogClickListener okayClicked) {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_ytd_sales);

        final EditText txt_value;
        final TextView txt_cancel, txt_add, txt_description;
        txt_value=(EditText)dialog.findViewById(R.id.txt_value);

        txt_cancel=(TextView)dialog.findViewById(R.id.txt_cancel);
        txt_add=(TextView)dialog.findViewById(R.id.txt_add);

        txt_description=(TextView)dialog.findViewById(R.id.txt_description);
        txt_description.setText(String.format("What's your %s year YTD", title));

        View.OnClickListener cancelClicked=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        };

        txt_cancel.setOnClickListener(cancelClicked);

        View.OnClickListener okayButClicked=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
               // MyUtils.startActivity(context, LineRevenueActivity.class);
            }
        };

        txt_add.setOnClickListener(okayButClicked);

        dialog.show();
    }
    public static void addFragment(FragmentTransaction transaction, Fragment currentFragment, Fragment targetFragment, String tag) {

        //transaction.setCustomAnimations(0,0,0,0);
        if(currentFragment!=null) transaction.hide(currentFragment);
        // use a fragment tag, so that later on we can find the currently displayed fragment
        if(targetFragment.isAdded()) {
            transaction.show(targetFragment).commit();
        }else {
            transaction.add(R.id.fragment_place, targetFragment, tag)
                    .addToBackStack(tag)
                    .commit();
        }
    }
    public static RevenueFilter initializeFilter() {
        String todaysDate=HelperMethods.getTodayDate();
        RevenueFilter filter=new RevenueFilter();
        filter.setSort("desc");
        int year=HelperMethods.getCurrentYear(HelperMethods.getTodayDate());
        filter.setStart_date(year+"-"+HelperMethods.getNumericMonthOfTheYear(todaysDate)+"-01");
        int endMonth=Arrays.asList(HelperMethods.MONTHS_LIST).indexOf(HelperMethods.getMonthOfTheYear(todaysDate));
        endMonth=endMonth+2;
        if(endMonth>12) endMonth=1;
        filter.setEnd_date(year+"-"+endMonth+"-01");
        return filter;
    }

    public static void setColorTheme(Activity activity, int source, View backgroundView) {

        int color=R.color.blue, backgroundDrawable=R.drawable.background_blue;
        if(backgroundView==null) {
            backgroundView= activity.getWindow().getDecorView().getRootView();
        }
        switch (source) {
            case Constants.SALES_SOURCE:
                color=R.color.green_color1;
                backgroundDrawable=R.mipmap.background_green;

                break;
            case Constants.LINE_SOURCE:
                color=R.color.blue;
                backgroundDrawable=R.drawable.background_blue;
                break;
            case Constants.COWORKER_SOURCE:
                color=R.color.gradient;
                backgroundDrawable=R.drawable.background_gradient;
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(color));
        }
        backgroundView.setBackground(activity.getResources().getDrawable(backgroundDrawable));
    }
    public static void showNoResults(View parentView, int viewID) {
        parentView.findViewById(viewID).setVisibility(View.VISIBLE);
    }
    public static void hideNoResults(View parentView) {
        parentView.findViewById(R.id.no_results).setVisibility(View.GONE);
    }

}
