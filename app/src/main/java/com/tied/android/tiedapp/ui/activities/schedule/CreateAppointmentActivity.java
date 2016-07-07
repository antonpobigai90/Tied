package com.tied.android.tiedapp.ui.activities.schedule;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.schedule.AppointmentCalendarFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.CreateAppointmentFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.CreateScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.ScheduleSuggestionFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;


/**
 * Created by Daniel on 5/3/2016.
 */
public class CreateAppointmentActivity extends FragmentActivity implements FragmentIterationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = CreateAppointmentFragment.class
            .getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private static final int FINE_LOCATION_PERMISSION = 1;

    private Fragment fragment = null;
    public Fragment profileFragment = null;
    private int fragment_index = 0;

    public Coordinate coordinate;

    User user;
    public Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        user = User.getUser(getApplicationContext());
        Client client = (Client) getIntent().getSerializableExtra(Constants.CLIENT_DATA);
        bundle = new Bundle();
        Gson gson = new Gson();
        String user_json = gson.toJson(user);
        String client_json = gson.toJson(client);

        bundle.putString(Constants.USER_DATA, user_json);
        bundle.putString(Constants.CLIENT_DATA, client_json);
        launchFragment(Constants.CreateAppointment, bundle);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {
        Log.d(TAG, " onFragmentInteraction " + action);
        launchFragment(action, bundle);
    }

    @Override
    public void onBackPressed() {
        if(fragment_index == Constants.CreateAppointment){
            finish();
        }
    }

    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;

        switch (pos) {
            case Constants.CreateAppointment:
                fragment = new CreateAppointmentFragment();
                break;
            case Constants.CreateSchedule:
                fragment = new CreateScheduleFragment();
                break;
            case Constants.AppointmentCalendar:
                fragment = new AppointmentCalendarFragment();
                break;
            case Constants.ScheduleSuggestions:
                fragment = new ScheduleSuggestionFragment();
                break;
            default:
                finish();
        }

        fragment.setArguments(bundle);
        if (fragment != null) {
            Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            while (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit)
            ft.replace(R.id.fragment_place, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "connected");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "connection failed");
    }

    protected void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            String lat = String.valueOf(location.getLatitude());
            String lng = String.valueOf(location.getLongitude());
            coordinate = new Coordinate(Double.parseDouble(lat), Double.parseDouble(lng));
            Log.d(TAG, "location coordinate ....."+coordinate);
        } else {
            coordinate = null;
            Log.d(TAG, "location is null ...............");
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case FINE_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    startLocationUpdates();
                } else {
                    // permission denied
                }
            }
        }
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

}
