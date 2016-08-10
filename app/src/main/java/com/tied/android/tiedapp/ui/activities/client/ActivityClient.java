package com.tied.android.tiedapp.ui.activities.client;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import bolts.AppLink;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.GeneralSelectObjectActivity;
import com.tied.android.tiedapp.ui.adapters.MyClientLineAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.ui.fragments.client.ClientMapFragment;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

/**
 * Created by femi on 8/10/2016.
 */
public class ActivityClient extends Activity implements OnMapReadyCallback {
    MapFragment mMapFragment;
    String TAG="ACTIVIY_CLINET";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_client_layout);
         mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        loadClients(googleMap);
    }


    public void loadClients(final GoogleMap googleMap) {
        final User user=MyUtils.getUserLoggedIn();
        ClientLocation clientLocation = new ClientLocation();
        clientLocation.setDistance("100000"+ MyUtils.getPreferredDistanceUnit());
        MyUtils.setCurrentLocation(new Coordinate(33.894212, -84.231574));
        Coordinate coordinate = MyUtils.getCurrentLocation();
        if( coordinate == null ){
            coordinate = user.getOffice_address().getCoordinate();
        }
        clientLocation.setCoordinate(coordinate);

        ClientApi clientApi =  MainApplication.getInstance().getRetrofit().create(ClientApi.class);
        Call<ClientRes> response = clientApi.getClientsByLocation(user.getToken(), clientLocation);
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if ( this == null ) return;
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
                if(clientRes.isAuthFailed()){
                    User.LogOut(getApplicationContext());
                }
                else if(clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200){
                    ArrayList<Client> clients = clientRes.getClients();

                    boolean centered=false;
                    for(Client client:clients) {
                        Logger.write(client.toString());
                        LatLng latLng=new LatLng(client.getAddress().getCoordinate().getLat(),
                                client.getAddress().getCoordinate().getLon());
                        if(!centered) {
                            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(33.8960935, -84.2319696) , 5.0f) );
                            centered=true;
                        }
                        googleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_white)).position(latLng).title(client.getCompany()==null?client.getFull_name():client.getCompany()));
                       client.setLogo(user.getAvatar());
                        Logger.write(TAG + " "+user.getAvatar());
                        if(client.getLogo()!=null && !client.getLogo().isEmpty()) MyPicassoTarget.load(ActivityClient.this, googleMap, client.getLogo(), latLng);

                    }

                }else{
                    Logger.write(clientRes.getMessage());
                    MyUtils.showToast(getString(R.string.connection_error));
                }
               // Log.d(TAG + " onResponse", resResponse.body().toString());
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
    static class MyPicassoTarget  {

        public static void load(Context c, final GoogleMap googleMap, String url,  final LatLng latLng) {
           Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    googleMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap)).position(latLng));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
//Logger.write(url);
            Picasso.with(c).load(url).into(target);
        }
    }
    public void goBack(View v) {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

