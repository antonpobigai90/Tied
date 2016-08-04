package com.tied.android.tiedapp.ui.fragments.schedule;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.MyAsyncTask;
import com.tied.android.tiedapp.customs.model.ScheduleNotifyModel;
import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.ScheduleRes;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.schedule.TimeRange;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.fragments.DatePickerFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.DialogUtils;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.ScheduleNotifyDialog;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import me.tittojose.www.timerangepicker_library.TimeRangePickerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.imanoweb.calendarview.CustomCalendarView;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateAppointmentFragment extends Fragment implements View.OnClickListener,
        TimeRangePickerDialog.OnTimeRangeSelectedListener, ScheduleNotifyDialog.SelectedListener {

    public static final String TAG = CreateAppointmentFragment.class
            .getSimpleName();

    TextView txt_title, txt_description, txt_date, txt_time, txt_creative_co_op, txt_reminder, txt_date_selected;
    ImageView img_avatar, img_plus_date, img_plus1, img_location, img_reminder, img_close;
    private EditText street, city, zip, state;

    private Bundle bundle;
    private User user;

    private Schedule schedule;
    private Client client;
    private Location location;
    int notify_id = 1;
    private ScheduleNotifyModel scheduleNotifyModel;

    private String endTimeText, startTimeText, dateText, titleText, streetText, cityText, stateText, zipText;
    private TextView txt_create_schedule;
    RelativeLayout layout_date, layout_time, layout_reminder;

    ScheduleNotifyDialog alert;

    private FragmentIterationListener fragmentIterationListener;

    public CreateAppointmentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_appointment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIterationListener) {
            fragmentIterationListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action, Bundle bundle) {
        if (fragmentIterationListener != null) {
            fragmentIterationListener.OnFragmentInteractionListener(action, bundle);
        }
    }

    public void initComponent(View view) {

        layout_date = (RelativeLayout) view.findViewById(R.id.layout_date);
        layout_time = (RelativeLayout) view.findViewById(R.id.layout_time);
        layout_reminder = (RelativeLayout) view.findViewById(R.id.layout_reminder);

        layout_date.setOnClickListener(this);
        layout_time.setOnClickListener(this);
        layout_reminder.setOnClickListener(this);

        txt_create_schedule = (TextView) view.findViewById(R.id.txt_create_schedule);
        txt_create_schedule.setOnClickListener(this);

        txt_creative_co_op = (TextView) view.findViewById(R.id.txt_creative_co_op);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_description = (TextView) view.findViewById(R.id.txt_description);
        txt_date = (TextView) view.findViewById(R.id.date);
        txt_date_selected = (TextView) view.findViewById(R.id.date_selected);
        txt_time = (TextView) view.findViewById(R.id.time);
        txt_reminder = (TextView) view.findViewById(R.id.reminder);

        street = (EditText) view.findViewById(R.id.street);
        state = (EditText) view.findViewById(R.id.state);
        city = (EditText) view.findViewById(R.id.city);
        zip = (EditText) view.findViewById(R.id.zip);

        img_close = (ImageView) view.findViewById(R.id.img_close);
        img_close.setOnClickListener(this);

        img_avatar = (ImageView) view.findViewById(R.id.img_avatar_schedule);

        img_plus_date = (ImageView) view.findViewById(R.id.img_plus_date);
        img_plus_date.setOnClickListener(this);


        img_plus1 = (ImageView) view.findViewById(R.id.img_plus1);
        img_plus1.setOnClickListener(this);

        img_reminder = (ImageView) view.findViewById(R.id.img_reminder);
        img_reminder.setOnClickListener(this);

        bundle = getArguments();
        if (bundle != null) {
            Log.d(TAG, "bundle not null");
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            String client_json = bundle.getString(Constants.CLIENT_DATA);
            user = gson.fromJson(user_json, User.class);
            client = gson.fromJson(client_json, Client.class);
            txt_creative_co_op.setText(client.getCompany());

            String schedule_json = bundle.getString(Constants.SCHEDULE_DATA);
            Log.d(TAG, "schedule_json "+schedule_json);
            if(schedule_json != null){
                schedule = gson.fromJson(schedule_json, Schedule.class);
                txt_title.setText(schedule.getTitle());
                street.setText(schedule.getLocation().getStreet());
                city.setText(schedule.getLocation().getCity());
                zip.setText(schedule.getLocation().getZip());
                state.setText(schedule.getLocation().getState());
                txt_time.setText(schedule.getTime_range().getRange());
                txt_date_selected.setText(schedule.getDate());
                txt_date.setText(HelperMethods.getFormatedDate(schedule.getDate()));
                txt_create_schedule.setText("UPDATE SCHEDULE");

            }else{
                street.setText(client.getAddress().getStreet());
                city.setText(client.getAddress().getCity());
                zip.setText(client.getAddress().getZip());
                state.setText(client.getAddress().getState());
            }

            String logo = client.getLogo().equals("") ? null  : client.getLogo();
            Picasso.with(getActivity()).
                    load(logo)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            if (bitmap != null) {
                                img_avatar.setImageBitmap(bitmap);
                            } else {
                                img_avatar.setImageResource(R.mipmap.default_avatar);
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            TimeRangePickerDialog tpd = (TimeRangePickerDialog) getActivity().getSupportFragmentManager()
                    .findFragmentByTag(TAG);
            if (tpd != null) {
                tpd.setOnTimeRangeSetListener(this);
            }
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.img_close:
                intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
                break;
            case R.id.img_plus_date:
//                nextAction(Constants.AppointmentCalendar, bundle);
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
            case R.id.img_plus1:
                TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(
                        CreateAppointmentFragment.this, false);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TAG);
                break;
            case R.id.img_reminder:
                ScheduleNotifyDialog alert = new ScheduleNotifyDialog();
                alert.showDialog(this);
                break;
            case R.id.layout_date:
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
            case R.id.layout_time:
                TimeRangePickerDialog timePickerDialog2 = TimeRangePickerDialog.newInstance(
                        CreateAppointmentFragment.this, false);
                timePickerDialog2.show(getActivity().getSupportFragmentManager(), TAG);
                break;
            case R.id.layout_reminder:
                ScheduleNotifyDialog alert2 = new ScheduleNotifyDialog();
                alert2.showDialog(this);
                break;
            case R.id.txt_create_schedule:
                if (validated()) {
                    new GeocodeAsyncTask().execute();
                } else {
                    Toast.makeText(getActivity(), "Input not filled appropiately", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public String timeFormat(String time){
        String[] split_hour_min = time.split(":");
        String hour = String.format("%02d", Integer.parseInt(split_hour_min[0]));
        String min = String.format("%02d", Integer.parseInt(split_hour_min[1]));
        return hour+":"+min;
    }

    public boolean validated() {
        dateText = txt_date_selected.getText().toString();
        titleText = txt_title.getText().toString();
        streetText = street.getText().toString();
        cityText = city.getText().toString();
        zipText = zip.getText().toString();
        stateText = state.getText().toString();
        location = new Location(cityText, zipText, stateText, streetText);

        String range = txt_time.getText().toString();
        String[] time = range.split("-");
        if(time.length == 2){
            String from = time[0].replace(" ", "");
            String to = time[1].replace(" ", "");
            startTimeText = timeFormat(from);
            endTimeText = timeFormat(to);
        }

        return dateText != null && endTimeText!= null && startTimeText != null;
    }

    @Override
    public void selectedNow(ScheduleNotifyModel scheduleNotifyModel) {
        String text_notify = "Notify me " + scheduleNotifyModel.getTxt_notify() + " before time";
        txt_reminder.setText(text_notify);
        notify_id = scheduleNotifyModel.getId();
    }

    class GeocodeAsyncTask extends MyAsyncTask {

        String errorMessage = "";
        @Override
        protected void onPreExecute() {
            DialogUtils.displayProgress(getActivity());
        }

        @Override
        protected Address doInBackground(Void... params) {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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
            if (getActivity() == null) return;
            if (address != null) {
                Coordinate coordinate = new Coordinate(address.getLatitude(), address.getLongitude());
                location.setCoordinate(coordinate);
                if(schedule == null){
                    createAppointment();
                }else {
                    updateAppointment();
                }
            } else {
                DialogUtils.closeProgress();
                Toast.makeText(getActivity(), "sorry location cannot be found in map", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void createAppointment() {
        final Schedule schedule = new Schedule();
        schedule.setTitle(titleText);
        schedule.setClient_id(client.getId());
        schedule.setUser_id(user.getId());
        schedule.setVisited(true);
        schedule.setReminder(notify_id);
        TimeRange timeRange = new TimeRange(startTimeText, endTimeText);
        schedule.setTime_range(timeRange);
        schedule.setEnd_time(endTimeText);
        schedule.setDate(dateText);
        schedule.setLocation(location);

        Log.d(TAG + " schedule", schedule.toString());

        DialogUtils.displayProgress(getActivity());
        ScheduleApi scheduleApi = MainApplication.getInstance().getRetrofit().create(ScheduleApi.class);
        Call<ScheduleRes> response = scheduleApi.createSchedule(user.getToken(), schedule);
        response.enqueue(new Callback<ScheduleRes>() {
            @Override
            public void onResponse(Call<ScheduleRes> call, Response<ScheduleRes> scheduleResResponse) {
                if (getActivity() == null) return;
                ScheduleRes scheduleRes = scheduleResResponse.body();
                Log.d(TAG + " onFailure", scheduleRes.toString());
                if (scheduleRes.isAuthFailed()) {
                    DialogUtils.closeProgress();
                    User.LogOut(getActivity());
                } else if (scheduleRes.get_meta() != null && scheduleRes.get_meta().getStatus_code() == 201) {
                    Log.d(TAG + " Schedule", scheduleRes.getSchedule().toString());
                    Gson gson = new Gson();
                    Schedule mainSchedule = scheduleRes.getSchedule();
                    String schedule_string = gson.toJson(mainSchedule, Schedule.class);
                    bundle.putSerializable(Constants.SCHEDULE_DATA, schedule_string);
                    Schedule.scheduleCreated(getActivity().getApplicationContext());
                    DialogUtils.closeProgress();
                    nextAction(Constants.ScheduleSuggestions, bundle);
                } else {
                    DialogUtils.closeProgress();
                    nextAction(Constants.CreateSchedule, bundle);
                    Toast.makeText(getActivity(), scheduleRes.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ScheduleRes> ScheduleResponseCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    public void updateAppointment() {
        schedule.setTitle(titleText);
        schedule.setClient_id(client.getId());
        schedule.setUser_id(user.getId());
        schedule.setVisited(true);
        schedule.setReminder(notify_id);
        TimeRange timeRange = new TimeRange(startTimeText, endTimeText);
        schedule.setTime_range(timeRange);
        schedule.setEnd_time(endTimeText);
        schedule.setDate(dateText);
        schedule.setLocation(location);

        Log.d(TAG + " schedule", schedule.toString());

        DialogUtils.displayProgress(getActivity());
        final ScheduleApi scheduleApi = MainApplication.getInstance().getRetrofit().create(ScheduleApi.class);
        Call<ScheduleRes> response = scheduleApi.updateSchedule(user.getToken(), schedule.getId(), schedule);
        response.enqueue(new Callback<ScheduleRes>() {
            @Override
            public void onResponse(Call<ScheduleRes> call, Response<ScheduleRes> scheduleResResponse) {
                if (getActivity() == null) return;
                ScheduleRes scheduleRes = scheduleResResponse.body();
                if (scheduleRes != null && scheduleRes.isAuthFailed()){
                    DialogUtils.closeProgress();
                    User.LogOut(getActivity());
                }
                else if (scheduleRes != null && scheduleRes.get_meta() != null && scheduleRes.get_meta().getStatus_code() == 200) {
                    Log.d(TAG + " Schedule", scheduleRes.getSchedule().toString());
                    Gson gson = new Gson();
                    Schedule updatedSchedule = scheduleRes.getSchedule();
                    if(updatedSchedule.getId().equals(schedule.getId())){
                        String schedule_string = gson.toJson(schedule, Schedule.class);
                        bundle.putSerializable(Constants.SCHEDULE_DATA, schedule_string);
                        Schedule.scheduleCreated(getActivity().getApplicationContext());
                        DialogUtils.closeProgress();
                        nextAction(Constants.ScheduleSuggestions, bundle);
                    }
                } else {
                    Toast.makeText(getActivity(), scheduleRes.toString(), Toast.LENGTH_LONG).show();
                    DialogUtils.closeProgress();
                }
            }

            @Override
            public void onFailure(Call<ScheduleRes> ScheduleResponseCall, Throwable t) {
                Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    @Override
    public void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin) {
        startTimeText = startHour + ":" + startMin;
        endTimeText = endHour + ":" + endMin;
        txt_time.setText(startTimeText + "-" + endTimeText);
    }
}