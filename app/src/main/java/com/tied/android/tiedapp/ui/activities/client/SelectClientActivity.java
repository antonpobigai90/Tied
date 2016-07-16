package com.tied.android.tiedapp.ui.activities.client;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.fragments.client.SelectClientDistanceFragment;
import com.tied.android.tiedapp.ui.fragments.client.SelectClientListFragment;
import com.tied.android.tiedapp.ui.fragments.client.SelectClientMapFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.DemoData;

public class SelectClientActivity extends AppCompatActivity implements FragmentIterationListener, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String TAG = SelectClientActivity.class
            .getSimpleName();

    public Coordinate coordinate;

    private GoogleApiClient mGoogleApiClient;
    private static final int FINE_LOCATION_PERMISSION = 1;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private Bundle bundle;
    private User user;

    LinearLayout tab_map, tab_last_visited,tab_distance;
    TextView map_indicator, last_visited_indicator,distance_indicator;
    TextView txt_map, txt_last_visited,txt_distance;

    private ImageView img_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_select_client);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        tab_map = (LinearLayout) findViewById(R.id.tab_map);
        tab_map.setOnClickListener(this);

        tab_last_visited = (LinearLayout) findViewById(R.id.tab_last_visited);
        tab_last_visited.setOnClickListener(this);

        tab_distance = (LinearLayout) findViewById(R.id.tab_distance);
        tab_distance.setOnClickListener(this);

        map_indicator = (TextView) findViewById(R.id.map_indicator);
        last_visited_indicator = (TextView) findViewById(R.id.last_visited_indicator);
        distance_indicator = (TextView) findViewById(R.id.distance_indicator);

        txt_distance = (TextView) findViewById(R.id.txt_distance);
        txt_map = (TextView) findViewById(R.id.txt_map);
        txt_last_visited = (TextView) findViewById(R.id.txt_last_visited);

        user = User.getUser(getApplicationContext());
        bundle = new Bundle();
        Gson gson = new Gson();
        String user_json = gson.toJson(user);
        bundle.putString(Constants.USER_DATA, user_json);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        if (mViewPager != null) {
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.setCurrentItem(1);
            last_visited_indicator.setVisibility(View.VISIBLE);
            txt_last_visited.setTextColor(getResources().getColor(R.color.button_bg));
        }

        onCustomSelected(mViewPager);
    }


    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab_map:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tab_last_visited:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tab_distance:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.img_close:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }


    public void onCustomSelected(ViewPager vpPager){
        // Attaching the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(SelectClientActivity.this,"Selected page position: " + position, Toast.LENGTH_SHORT).show();

                switch(position){
                    case 0:
                        map_indicator.setVisibility(View.VISIBLE);
                        last_visited_indicator.setVisibility(View.GONE);
                        distance_indicator.setVisibility(View.GONE);

                        txt_map.setTextColor(getResources().getColor(R.color.button_bg));
                        txt_last_visited.setTextColor(getResources().getColor(R.color.semi_transparent_black));
                        txt_distance.setTextColor(getResources().getColor(R.color.semi_transparent_black));
                        break;
                    case 1:
                        last_visited_indicator.setVisibility(View.VISIBLE);
                        map_indicator.setVisibility(View.GONE);
                        distance_indicator.setVisibility(View.GONE);

                        txt_map.setTextColor(getResources().getColor(R.color.semi_transparent_black));
                        txt_last_visited.setTextColor(getResources().getColor(R.color.button_bg));
                        txt_distance.setTextColor(getResources().getColor(R.color.semi_transparent_black));
                        break;
                    case 2:
                        distance_indicator.setVisibility(View.VISIBLE);
                        map_indicator.setVisibility(View.GONE);
                        last_visited_indicator.setVisibility(View.GONE);

                        txt_map.setTextColor(getResources().getColor(R.color.semi_transparent_black));
                        txt_last_visited.setTextColor(getResources().getColor(R.color.semi_transparent_black));
                        txt_distance.setTextColor(getResources().getColor(R.color.button_bg));
                        break;

                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }


    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Log.d(TAG, "position : "+position);
            switch (position){
                case 0:
                    fragment = new SelectClientMapFragment();
                    break;
                case 1:
                    fragment = new SelectClientListFragment();
                    break;
                case 2:
                    fragment = new SelectClientDistanceFragment();
                    break;
            }
            assert fragment != null;
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return DemoData.select_client_layout.length;
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
