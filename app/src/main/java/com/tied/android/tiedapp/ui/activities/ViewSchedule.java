package com.tied.android.tiedapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Location;

/**
 * Created by Emmanuel on 6/23/2016.
 */
public class ViewSchedule extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap myMap;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schedule_view);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//        }else {
//
//        }

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        myMap = googleMap;

        myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        myMap.addMarker(new MarkerOptions().position(sydney));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        myMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

    }


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        boolean not_first_time_showing_info_window;

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.schedule_map_info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {


            TextView line = ((TextView)myContentsView.findViewById(R.id.line));
            TextView address = ((TextView)myContentsView.findViewById(R.id.address));
            TextView distance = ((TextView)myContentsView.findViewById(R.id.distance));

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }





}
