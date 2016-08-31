package com.tied.android.tiedapp.ui.activities.schedule;

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
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
<<<<<<< HEAD
import com.tied.android.tiedapp.customs.MyStringAsyncTask;
import com.tied.android.tiedapp.objects.client.Client;
=======
import com.tied.android.tiedapp.objects.Coordinate;
>>>>>>> ace93a55844f96cd8945b04589c755b6f27c6121
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.MyUtils;

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
    TextView dayTV, monthTV;


    private TextView description, temperature, title, schedule_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schedule_view);

        user = User.getUser(getApplicationContext());

        client = (Client) getIntent().getSerializableExtra(Constants.CLIENT_DATA);
        schedule = (Schedule) getIntent().getSerializableExtra(Constants.SCHEDULE_DATA);

        final MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(ViewSchedule.this);

        schedule_title = (TextView) findViewById(R.id.schedule_title);
        description = (TextView) findViewById(R.id.description);
        title = (TextView) findViewById(R.id.title);
        temperature = (TextView) findViewById(R.id.weather);

        title.setText(schedule.getTitle());
        schedule_title.setText(schedule.getTitle());
        description.setText(schedule.getDescription());

        dayTV=(TextView) findViewById(R.id.day);

            long diff_in_date = HelperMethods.getDateDifferenceWithToday(schedule.getDate());

            String day = String.format("%02d", HelperMethods.getDayFromSchedule(schedule.getDate()));
            String week_day = MyUtils.getWeekDay(schedule);
        dayTV.setText(day);




        String timeRange = MyUtils.getTimeRange(schedule);
       // time.setText(timeRange);

        final RequestBuilder weather = new RequestBuilder();
        Request request = new Request();
        request.setLat(""+schedule.getLocation().getCoordinate().getLat());
        request.setLng(""+schedule.getLocation().getCoordinate().getLon());
        request.setUnits(Request.Units.US);
        request.setLanguage(Request.Language.ENGLISH);
        request.addExcludeBlock(Request.Block.CURRENTLY);

        weather.getWeather(request, new Callback<WeatherResponse>() {
            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                Log.d(TAG, "temperature : " + weatherResponse.getDaily().getData().get(0).getApparentTemperatureMax() + "");
                int temp_max = (int) weatherResponse.getDaily().getData().get(0).getApparentTemperatureMax();
                temp_max = (int) HelperMethods.convertFahrenheitToCelcius(temp_max);
                temperature.setText(temp_max + "°");
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

<<<<<<< HEAD
        LatLng location = new LatLng(schedule.getLocation().getCoordinate().getLat(), schedule.getLocation().getCoordinate().getLon());
        Marker melbourne = myMap.addMarker(new MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
=======
        Coordinate coordinate = client.getAddress().getCoordinate();
        LatLng latLng = new LatLng(coordinate.getLat(), coordinate.getLon());

        Marker melbourne = myMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
>>>>>>> ace93a55844f96cd8945b04589c755b6f27c6121

        myMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        melbourne.showInfoWindow();

    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        boolean not_first_time_showing_info_window;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.schedule_map_info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {


            TextView line = ((TextView) myContentsView.findViewById(R.id.line));
            TextView address = ((TextView) myContentsView.findViewById(R.id.address));
            TextView distance = ((TextView) myContentsView.findViewById(R.id.distance));
            final ImageView image = ((ImageView) myContentsView.findViewById(R.id.image));
            try {
                line.setText(client.getCompany());
                address.setText(schedule.getLocation().getLocationAddress());
                distance.setText("0.2 miles");

                MyUtils.Picasso.displayImage(user.getAvatar(), image);

            } catch (Exception e) {

            }
            return myContentsView;
        }




        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
