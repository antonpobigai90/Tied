package com.tied.android.tiedapp.ui.activities.schedule;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.util.HelperMethods;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Emmanuel on 6/23/2016.
 */
public class ViewSchedule extends AppCompatActivity implements OnMapReadyCallback {
    public static final String TAG = ViewSchedule.class
            .getSimpleName();

    GoogleMap myMap;
    private Location mLocation;
    private User user;
    private Client client;
    private Schedule schedule;

    private TextView description, temperature, title, schedule_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schedule_view);

        user = User.getUser(getApplicationContext());
        client = (Client) getIntent().getSerializableExtra(Constants.CLIENT_DATA);
        schedule = (Schedule) getIntent().getSerializableExtra(Constants.SCHEDULE_DATA);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        schedule_title = (TextView) findViewById(R.id.schedule_title);
        description = (TextView) findViewById(R.id.description);
        title = (TextView) findViewById(R.id.title);
        temperature = (TextView) findViewById(R.id.weather);

        title.setText(schedule.getTitle());
        schedule_title.setText(schedule.getTitle());
        description.setText(schedule.getDescription());

        final RequestBuilder weather = new RequestBuilder();
        Request request = new Request();
        request.setLat("32.00");
        request.setLng("-81.00");
        request.setUnits(Request.Units.US);
        request.setLanguage(Request.Language.ENGLISH);
        request.addExcludeBlock(Request.Block.CURRENTLY);

        weather.getWeather(request, new Callback<WeatherResponse>() {
            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                Log.d(TAG, "temperature : "+weatherResponse.getDaily().getData().get(0).getApparentTemperatureMax()+"");
                int temp_max = (int) weatherResponse.getDaily().getData().get(0).getApparentTemperatureMax();
                temp_max = (int) HelperMethods.convertFahrenheitToCelcius(temp_max);
                temperature.setText(temp_max+"°");
            }
            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, "Error while calling: " + retrofitError.getUrl());
            }
        });

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        myMap = googleMap;

        myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
        Marker melbourne = myMap.addMarker(new MarkerOptions()
                            .position(MELBOURNE)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MELBOURNE,15));

        myMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        melbourne.showInfoWindow();

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
            final ImageView image = ((ImageView) myContentsView.findViewById(R.id.image));

            line.setText(client.getCompany());
            address.setText(client.getAddress().getStreet());
            distance.setText("0.2 miles");

            Picasso.with(ViewSchedule.this).
                    load(Constants.GET_AVATAR_ENDPOINT+"avatar_"+user.getId()+".jpg")
                    .resize(35,35)
                    .into(new Target() {
                        @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            if (bitmap != null){
                                image.setImageBitmap(bitmap);
                            }else{
                                image.setImageResource(R.mipmap.default_avatar);
                            }
                        }
                        @Override public void onBitmapFailed(Drawable errorDrawable) { }
                        @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
                    });

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
