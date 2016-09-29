package com.tied.android.tiedapp.ui.activities.schedule;


import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tied.android.tiedapp.ui.activities.client.ViewClientActivity;
import com.tied.android.tiedapp.util.Logger;
import org.apache.commons.lang.time.DateUtils;

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

import com.tied.android.tiedapp.objects.client.Client;

import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.MyUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by Emmanuel on 6/23/2016.
 */

public class ViewSchedule extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    public static final String TAG = ViewSchedule.class
            .getSimpleName();

    GoogleMap myMap;
    private Location mLocation;
    private User user;
    private Client client;
    private Schedule schedule;
    TextView dayTV, weekTV, timeRange;

    Bundle bundle;
    private static ViewSchedule viewSchedule;


    private TextView description, temperature,  schedule_title, weatherInfo;

    public static ViewSchedule getInstance() {
        return viewSchedule;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schedule_view);

        viewSchedule=this;

        user = User.getUser(getApplicationContext());


        client = (Client) getIntent().getSerializableExtra(Constants.CLIENT_DATA);
        schedule = (Schedule) getIntent().getSerializableExtra(Constants.SCHEDULE_DATA);
        bundle=new Bundle();
        bundle.putSerializable(Constants.CLIENT_DATA, client);
        bundle.putSerializable(Constants.SCHEDULE_DATA, schedule);

        Logger.write(client.toString());
        final MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(ViewSchedule.this);

        schedule_title = (TextView) findViewById(R.id.schedule_title);
        description = (TextView) findViewById(R.id.description);
        //title = (TextView) findViewById(R.id.title);
        temperature = (TextView) findViewById(R.id.weather);

Logger.write(schedule.toString());
        //title.setText(schedule.getTitle());
        schedule_title.setText(schedule.getTitle());
        description.setText(schedule.getDescription());
        if(schedule.getDescription()==null || schedule.getDescription()=="") findViewById(R.id.description_section).setVisibility(View.GONE);

        if(schedule.getDescription()==null || schedule.getDescription().isEmpty()) description.setVisibility(View.GONE);

        dayTV=(TextView) findViewById(R.id.day);
        weekTV=(TextView) findViewById(R.id.week_day);
        timeRange =(TextView) findViewById(R.id.time_range);

            long diff_in_date = HelperMethods.getDateDifferenceWithToday(schedule.getDate());

            String day = String.format("%02d", HelperMethods.getDayFromSchedule(schedule.getDate()));
           // String week_day = MyUtils.getWeekDay(schedule);
        dayTV.setText(MyUtils.toNth(day));
       // long epoch=SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        long epoch = 0;
        Logger.write(schedule.getDate()+" "+schedule.getTime_range().getStart_time());
        timeRange.setText(schedule.getTime_range().getStart_time()+" - "+schedule.getTime_range().getEnd_time());
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(schedule.getDate()+" "+schedule.getTime_range().getStart_time()).getTime();
            System.out.println(epoch);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Logger.write(e);
        }
        //String timePassedString = ""+ android.text.format.DateUtils.getRelativeTimeSpanString(epoch, System.currentTimeMillis(), android.text.format.DateUtils.WEEK_IN_MILLIS);
        weekTV.setText(MyUtils.getWeekDay(schedule));
        weatherInfo=(TextView) findViewById(R.id.weather_info);


        findViewById(R.id.img_edit).setOnClickListener(this);



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

               // temp_max = (int) HelperMethods.convertFahrenheitToCelcius(temp_max);
                temperature.setText(temp_max + "°");
                weatherInfo.setText(weatherResponse.getDaily().getData().get(0).getSummary());
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

        LatLng location = new LatLng(schedule.getLocation().getCoordinate().getLat(), schedule.getLocation().getCoordinate().getLon());
        Marker melbourne = myMap.addMarker(new MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng location2=new LatLng(location.latitude+0.002, location.longitude);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location2, 15));

        myMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        myMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                MyUtils.startActivity(ViewSchedule.this, ViewClientActivity.class, bundle);
            }
        });
        melbourne.showInfoWindow();

    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        boolean not_first_time_showing_info_window;

        MyInfoWindowAdapter() {
            ContextThemeWrapper cw = new ContextThemeWrapper(
                   getApplicationContext(), R.style.Transparent);

            LayoutInflater inflater = (LayoutInflater) cw
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myContentsView = inflater.inflate(R.layout.schedule_map_info_window, null);
        }

        //// TODO: 9/2/2016  avater should be clients

        @Override
        public View getInfoWindow(Marker marker) {


            TextView line = ((TextView) myContentsView.findViewById(R.id.line));
            TextView address = ((TextView) myContentsView.findViewById(R.id.address));
            TextView distance = ((TextView) myContentsView.findViewById(R.id.distance));
            final ImageView image = ((ImageView) myContentsView.findViewById(R.id.image));

            //if(client==null) image.setVisibility(View.GONE);
            try {
                line.setText(MyUtils.getClientName(client));
                address.setText(schedule.getLocation().getLocationAddress());
                distance.setText(""+MyUtils.getDistance(MyUtils.getCurrentLocation(), schedule.getLocation().getCoordinate()));



                image.setImageResource(R.drawable.default_avatar);
                if(client.getLogo()!=null && client.getLogo()!="") {
                    Logger.write(client.toString());
                    MyUtils.Picasso.displayImage(client.getLogo(), image);
                }

            } catch (Exception e) {

            }
            return myContentsView;
        }





        @Override
        public View getInfoContents(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }
    public void callClient() {
        String phone = client.getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                onBackPressed();
                break;
            case R.id.img_edit:
                MyUtils.startActivity(this, CreateAppointmentActivity.class, bundle);
                //finish();
                break;
            case R.id.call_client:
                callClient();
                break;
        }
    }
}
