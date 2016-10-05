package com.tied.android.tiedapp.ui.fragments.client;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.client.ClientLocation;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.client.MapClientList;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.Logger;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by femi on 8/10/2016.
 */
public class ClientsMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener{

    MapFragment mMapFragment;
    String TAG = "ACTIVIY_CLINET";
    Map<String, Client> markerClientMap = new HashMap<String, Client>();
    User user;
    ArrayList<Client> clients=new ArrayList<>();
    GoogleMap googleMap;
    ImageView img_list_clients;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_client_map_layout, container, false);
        return view;
    }

    public static ClientsMapFragment newInstance(Bundle bundle, ArrayList<Client> clients) {
        ClientsMapFragment cmf=new ClientsMapFragment();
        cmf.setArguments(bundle);
        cmf.setClients(clients);
        return cmf;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }
    public void addClients(ArrayList<Client> clients) {
        this.clients.addAll( clients);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    public void initComponent(View view) {
        bundle=getArguments();
        user=MyUtils.getUserFromBundle(bundle);
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mMapFragment);
        fragmentTransaction.commit();

        mMapFragment.getMapAsync(this);
       // user = MyUtils.getUserLoggedIn();
    }

    public void loadClients() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()) == ConnectionResult.SUCCESS) {
            System.gc();
            googleMap.clear();
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Setting a custom info window adapter for the google map
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoContents(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoWindow(Marker marker) {

                //Logger.write(marker.getTag().toString());
                ContextThemeWrapper cw = new ContextThemeWrapper(
                       getActivity().getApplicationContext(), R.style.Transparent);

                //Client client=markerClientMap.get((String)marker.getTag());
                Client client;
                try {
                    client = clients.get((int) marker.getTag());
                    if (client == null) return null;
                }catch (Exception e) {
                    return null;
                }

                // Getting view from the layout file info_window_layout
                LayoutInflater inflater = (LayoutInflater) cw
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.map_marker_info_window, null);

                ImageView photo = (ImageView) v.findViewById(R.id.user_picture_iv);
                if (!client.getLogo().isEmpty())
                    MyUtils.Picasso.displayImage(client.getLogo(), photo);
                // Getting the position from the marker
                //LatLng latLng = arg0.getPosition();

                // Getting reference to the TextView to set latitude
                TextView tvName = (TextView) v.findViewById(R.id.full_name_tv);

                // Getting reference to the TextView to set longitude
                TextView tvAddress = (TextView) v.findViewById(R.id.address_tv);

                // Getting reference to the TextView to set longitude
                TextView tvDistance = (TextView) v.findViewById(R.id.distance);

                tvName.setText(client.getFull_name());
                tvAddress.setText(client.getAddress().getStreet() + " " + client.getAddress().getCity());

                tvDistance.setText(MyUtils.getDistance(MyUtils.getCurrentLocation(), client.getAddress().getCoordinate()));

                // Returning the view containing InfoWindow contents
                return v;

            }
        });

        // Adding and showing marker while touching the GoogleMap
        /*googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                // Clears any existing markers from the GoogleMap
              //  googleMap.clear();

                // Creating an instance of MarkerOptions to set position
               // MarkerOptions markerOptions = new MarkerOptions();

                // Setting position on the MarkerOptions
              //  markerOptions.position(arg0);

                // Animating to the currently touched position
               // googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));

                // Adding marker on the GoogleMap
                //Marker marker = googleMap.addMarker(markerOptions);

                // Showing InfoWindow on the GoogleMap
               // marker.showInfoWindow();

            }
        });*/

        loadClients(googleMap);
    }


    public void loadClients(final GoogleMap googleMap) {

        ClientLocation clientLocation = new ClientLocation();
        clientLocation.setDistance("100000" + MyUtils.getPreferredDistanceUnit());
        Coordinate coordinate = MyUtils.getCurrentLocation();
        if (coordinate == null) {
            coordinate = user.getOffice_address().getCoordinate();
        }
        clientLocation.setCoordinate(coordinate);

        ClientApi clientApi = MainApplication.createService(ClientApi.class);
        Call<ClientRes> response = clientApi.getClientsByLocation(user.getId(), clientLocation);
        response.enqueue(new Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, Response<ClientRes> resResponse) {
                if (this == null) return;
                DialogUtils.closeProgress();
                ClientRes clientRes = resResponse.body();
                if (clientRes.isAuthFailed()) {
                    //User.LogOut(getActivity().getApplicationContext());
                } else if (clientRes.get_meta() != null && clientRes.get_meta().getStatus_code() == 200) {
                    clients.addAll(clientRes.getClients());
                    googleMap.clear();
                    if(clients.size()==0) {
                        MyUtils.showToast("No clients found");
                        return;
                    }
                    Coordinate currentLocation=MyUtils.getCurrentLocation();
                    LatLng loc=new LatLng(currentLocation.getLat(), currentLocation.getLon());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 5.0f));
                    googleMap.addMarker(new MarkerOptions()
                            .position(loc));


                    boolean centered = false;
                    int i = -1;
                    for (Client client : clients) {
                        i++;
                        LatLng latLng = new LatLng(client.getAddress().getCoordinate().getLat(),
                                client.getAddress().getCoordinate().getLon());
                      /*  if (!centered) {
                            Logger.write(client.getLogo());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.8960935, -84.2319696), 3.0f));
                            centered = true;
                        }*/
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_white)).anchor(0.5f, 1f).position(latLng));
                        // client.setLogo(user.getAvatar());
                        marker.setTag(i);

                        if (client.getLogo() != null && !client.getLogo().isEmpty()) {
                            //Logger.write(TAG + " "+client.getLogo());
                            new MyPicassoTarget(googleMap, client);
                        }

                    }

                } else {
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_list_clients:
                MyUtils.startActivity(getActivity(), MapClientList.class);
                break;

        }
    }

    class MyPicassoTarget {

        public MyPicassoTarget(final GoogleMap googleMap, final Client client) {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Logger.write("Image loaded " + bitmap.getWidth());
                    Marker marker = googleMap.addMarker(new MarkerOptions().anchor(0.5f, 1.5f)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap)).position(new LatLng(client.getAddress().getCoordinate().getLat(), client.getAddress().getCoordinate().getLon())));
                    // marker.setTag(client.getId());
                    // markerClientMap.put(client.getId(), client);


                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Logger.write("Image not loaded ");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
//Logger.write(url);
            Picasso.with(getActivity()).load(client.getLogo()).centerCrop().transform(new CircleTransform()).resize(100, 100).into(target);
        }
    }

    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
