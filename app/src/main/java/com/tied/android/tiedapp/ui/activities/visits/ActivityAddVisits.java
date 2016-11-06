package com.tied.android.tiedapp.ui.activities.visits;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.MyAddressAsyncTask;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.Visit;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.objects.visit.Duration;
import com.tied.android.tiedapp.retrofits.services.VisitApi;
import com.tied.android.tiedapp.ui.dialogs.DatePickerFragment;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import me.tittojose.www.timerangepicker_library.TimeRangePickerDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by femi on 8/4/2016.
 */
@SuppressWarnings("ValidFragment")
public class ActivityAddVisits extends AppCompatActivity implements  View.OnClickListener, TimeRangePickerDialog.OnTimeRangeSelectedListener{

    public static final String TAG = ActivityAddVisits.class
            .getSimpleName();

    private User user;
    Client client;
    Location location;

    ImageView clientPhoto;
    TextView clientNameTV, dateTV, locationTV;
    EditText distance, titleET;
    float salesAmount=0.00f;
    String title="";
    RelativeLayout select_date, select_time;
    TextView time_selected, time;
    TextView date_selected;

   // AddSalesFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visits);

        user=MyUtils.getUserLoggedIn();

        initComponent();
    }
    private void initComponent() {

        clientPhoto=(ImageView)findViewById(R.id.client_photo);
        clientNameTV=(TextView)findViewById(R.id.client_name);
        dateTV=(TextView)findViewById(R.id.txt_date);
        titleET=(EditText)findViewById(R.id.title);
        locationTV = (TextView)findViewById(R.id.location);
        select_date = (RelativeLayout)findViewById(R.id.select_date);
        select_date.setOnClickListener(this);
        date_selected = (TextView)findViewById(R.id.date_selected);
        distance=(EditText)findViewById(R.id.distance);

        select_time = (RelativeLayout)findViewById(R.id.select_time);
        select_time.setOnClickListener(this);
        time_selected = (TextView)findViewById(R.id.time_selected);
        time = (TextView)findViewById(R.id.time);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_status_bar));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_client_layout:
                selectClient(client);
                break;
            case R.id.select_location_layout:
                MyUtils.showAddressDialog(this, "Visit Location", location, new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {
                        if(response!=null) {
                            location=(Location)response;
                            locationTV.setText(location.getLocationAddress());
                        }else{
                            locationTV.setText("Click to enter location");
                        }
                    }
                });
                break;
            case R.id.select_date:
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.show(this.getSupportFragmentManager(), "datePicker");
                break;
            case R.id.select_time:
                TimeRangePickerDialog timePickerDialog2 = TimeRangePickerDialog.newInstance(
                        ActivityAddVisits.this, false);
                timePickerDialog2.show(this.getSupportFragmentManager(), TAG);
                break;
            case R.id.add_button:
                title=titleET.getText().toString().trim();
                if(title.isEmpty()) {
                    MyUtils.showAlert(this, "You must enter the title for this visit");
                    return;
                }
                if(client==null) {
                    MyUtils.showAlert(this, "You must choose a client");
                    return;
                }
                if(locationTV.getText().toString().trim().isEmpty()) {
                    MyUtils.showAlert(this, "You must choose a location");
                    return;
                }
                if(date_selected.getText().toString().trim().isEmpty()) {
                    MyUtils.showAlert(this, "You must enter the date of this visit");
                    return;
                }

                String str_time = time.getText().toString();
                if(str_time==null || str_time.isEmpty()) {
                    MyUtils.showAlert(this, "You must enter the time for the visit");
                    return;
                }

                createVisits();

//                new GeocodeAsyncTask().execute();
                break;
        }
    }

    private void createVisits() {

        final Visit visit = new Visit();
        visit.setTitle(titleET.getText().toString());
        visit.setUser_id(user.getId());
        if(client!=null)    visit.setClient_id(client.getId());
        else visit.setClient_id(null);
        visit.setSchedule_id(null);
        visit.setAddress(location);
        visit.setDistance(distance.getText().toString());
        visit.setUnit("mi");
        visit.setVisit_date(date_selected.getText().toString());
        visit.setVisit_time(time.getText().toString());

        Duration duration = new Duration(5, 30);
        visit.setDuration(duration);

        VisitApi visitApi = MainApplication.createService(VisitApi.class, user.getToken());
        DialogUtils.displayProgress(this);
        Call<ResponseBody> response = visitApi.addVisit(visit);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (this == null) return;
                try {
                    GeneralResponse response =new GeneralResponse(resResponse.body());
                    if (response.isAuthFailed()) {
                        User.LogOut(ActivityAddVisits.this);
                        return;
                    }
                    _Meta meta=response.getMeta();
                    if(meta !=null && meta.getStatus_code()==201) {
                        DialogUtils.closeProgress();
                        finish();
                    }else{
                        MyUtils.showToast("Error encountered");
                        DialogUtils.closeProgress();
                    }

                }catch (IOException ioe) {
                    DialogUtils.closeProgress();
                    MyUtils.showToast("Error encountered. Please check your internet connection.");

                    Logger.write(ioe);
                }
                catch (Exception jo) {
                    DialogUtils.closeProgress();
                    MyUtils.showToast("Error encountered. Please check your internet connection.");

                    Logger.write(jo);
                }

            }
            @Override
            public void onFailure(Call<ResponseBody> ClientResponseCall, Throwable t) {
                Logger.write("Request failed: "+t.getCause());
                MyUtils.showConnectionErrorToast(ActivityAddVisits.this);
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin) {
        time.setText(String.format("%02d", startHour) + ":" + String.format("%02d", startMin));
    }

    class GeocodeAsyncTask extends MyAddressAsyncTask {

        String errorMessage = "";
        @Override
        protected void onPreExecute() {
            DialogUtils.displayProgress(ActivityAddVisits.this);
        }

        @Override
        protected Address doInBackground(Void... params) {
            Geocoder geocoder = new Geocoder(ActivityAddVisits.this, Locale.getDefault());
            List<Address> addresses = null;

            try {
                Log.d(TAG, location.getLocationAddress());
                addresses = geocoder.getFromLocationName(location.getLocationAddress(), 1);
            } catch (IOException e) {
                errorMessage = "Service not available";
                Log.e(TAG, errorMessage, e);
            }

            if (addresses != null && addresses.size() > 0)
                return addresses.get(0);

            return null;
        }

        protected void onPostExecute(Address address) {
            if (ActivityAddVisits.this == null) return;
            if (address != null) {
                Coordinate coordinate = new Coordinate(address.getLatitude(), address.getLongitude());
                location.setCoordinate(coordinate);
                createVisits();
            } else {
                DialogUtils.closeProgress();
                Toast.makeText(ActivityAddVisits.this, "sorry location cannot be found in map", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void selectClient(Client client) {
        MyUtils.initiateClientSelector(this, client, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.write(requestCode+" "+resultCode);
        if(requestCode==Constants.SELECT_CLIENT && resultCode==RESULT_OK) {
            client = (Client)(data.getSerializableExtra("selected"));
            clientNameTV.setText(MyUtils.getClientName(client));
            MyUtils.Picasso.displayImage(client.getLogo(), clientPhoto);
            Logger.write(client.toString());
        }
    }
    public void goBack(View v) {
        onBackPressed();
    }
}