package com.tied.android.tiedapp.ui.fragments.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by femi on 9/10/2016.
 */
public class MapboxFragment extends Fragment  implements View.OnClickListener, OnMapReadyCallback {

        MapView mapView;
        String TAG = "ACTIVIY_CLINET";
        Map<String, Client> markerClientMap = new HashMap<String, Client>();
        User user;
        ArrayList<Client> clients;

    ImageView img_list_clients;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //MapboxAccountManager.start(getActivity(), getString(R.string.mapbox_access_token));
        View view = inflater.inflate(R.layout.fragment_mapbox, container, false);
        mapView = (MapView) view.findViewById(R.id.mapview);
       // mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView.onCreate(getArguments());
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Create a mapView

        initComponent(view);
    }

    public void initComponent(View view) {
       /* mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);*/
        user = MyUtils.getUserLoggedIn();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        Logger.write("Femi it is ready");
       /* Coordinate location= MyUtils.getCurrentLocation();
        LatLng ll=new LatLng(location.getLat(), location.getLon());
        MarkerViewOptions marker = new MarkerViewOptions()
                .position(ll);

        mapboxMap.addMarker(marker);
        CameraPosition.Builder builder=new CameraPosition.Builder();
        builder.target(ll);*/
       // mapboxMap.setCameraPosition( builder.build());


    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
