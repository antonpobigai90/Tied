package com.tied.android.tiedapp.ui.fragments.schedule.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ScheduleDataModel;
import com.tied.android.tiedapp.customs.model.ScheduleTimeModel;
import com.tied.android.tiedapp.objects.responses.ScheduleRes;
import com.tied.android.tiedapp.objects.schedule.DateRange;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.schedule.ScheduleDate;
import com.tied.android.tiedapp.objects.schedule.TimeRange;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.ui.adapters.ScheduleListAdapter;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.DialogUtils;
import com.tied.android.tiedapp.util.HelperMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 7/15/2016.
 */
public abstract class SchedulesFragment extends Fragment implements View.OnClickListener {
    protected static final String TAG = SchedulesFragment.class
            .getSimpleName();

    String[] MONTHS_LIST = {"January", "Febebuary", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};
    String[] WEEK_LIST = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};


    public FragmentIterationListener mListener;

    protected ScheduleDate scheduleDate;

    protected ArrayList<ScheduleDataModel> schedules;
    protected ListView listView;

    protected TimeRange timeRange = null;
    protected DateRange dateRange = null;

    protected ScheduleListAdapter adapter;
    protected Bundle bundle;
    protected User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "Fragment changed");
        initComponent(view);
    }

    protected void initComponent(View view){
        listView = (ListView) view.findViewById(R.id.list);
        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            user = gson.fromJson(user_json, User.class);
            initSchedule();
        }
    }

    protected void initSchedule() {
        Log.d(TAG + " scheduleDate", scheduleDate.toString());
        ScheduleApi scheduleApi = MainApplication.getInstance().getRetrofit().create(ScheduleApi.class);
        Call<ScheduleRes> response = scheduleApi.getScheduleByDate(user.getToken(), scheduleDate);
        response.enqueue(new Callback<ScheduleRes>() {
            @Override
            public void onResponse(Call<ScheduleRes> call, Response<ScheduleRes> resResponse) {
                if (getActivity() == null) return;
                Log.d(TAG + "ScheduleRes", resResponse.toString());
                DialogUtils.closeProgress();
                ScheduleRes scheduleRes = resResponse.body();
                if (scheduleRes != null && scheduleRes.isAuthFailed()) {
                    User.LogOut(getActivity());
                } else if (scheduleRes != null && scheduleRes.get_meta() != null && scheduleRes.get_meta().getStatus_code() == 200) {
                    ArrayList<Schedule> scheduleArrayList = scheduleRes.getSchedules();
                    ArrayList<ScheduleDataModel> scheduleDataModels = null;
                    Log.d(TAG + " scheduleArrayList", scheduleArrayList.toString());
                    scheduleDataModels = parseSchedules(scheduleArrayList);

                    ScheduleListAdapter adapter = new ScheduleListAdapter(scheduleDataModels, getActivity());
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "encountered error with server", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ScheduleRes> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    private boolean isSameDay(String day1, String day2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = sdf.parse(day1);
            Date date2 = sdf.parse(day2);
            return date1.compareTo(date2) == 0;
        } catch (ParseException e) {
            return false;
        }
    }

    public ArrayList<ScheduleDataModel> parseSchedules(ArrayList<Schedule> scheduleArrayList) {

        Log.d(TAG + " parseSchedules", scheduleArrayList.toString());

        ArrayList<ScheduleDataModel> scheduleDataModels = new ArrayList<>();
        for (int i = 0; i < scheduleArrayList.size(); i++) {
            Schedule schedule = scheduleArrayList.get(i);

            ScheduleDataModel scheduleDataModel = new ScheduleDataModel();

            ScheduleTimeModel scheduleTimeModel = new ScheduleTimeModel(schedule.getId(),
                    schedule.getTitle(), schedule.getTime_range().getStart_time());

            ArrayList<ScheduleTimeModel> scheduleTimeModels = new ArrayList<ScheduleTimeModel>();
            scheduleTimeModels.add(scheduleTimeModel);
            for (int j = 1; j < scheduleArrayList.size(); j++) {
                Schedule this_schedule = scheduleArrayList.get(j);
                if (isSameDay(schedule.getDate(), this_schedule.getDate())) {
                    scheduleTimeModel = new ScheduleTimeModel(schedule.getId(),
                            schedule.getTitle(), schedule.getTime_range().getStart_time());
                    scheduleTimeModels.add(scheduleTimeModel);
                    scheduleArrayList.remove(j);
                }
            }

            String day = String.format("%02d", HelperMethods.getDayFromSchedule(schedule.getDate()));
            String week_day = WEEK_LIST[HelperMethods.getDayOfTheWeek(schedule.getDate()) - 1];

            scheduleDataModel.setScheduleTimeModel(scheduleTimeModels);
            scheduleDataModel.setTemperature("80");
            scheduleDataModel.setWeather("cloudy");
            scheduleDataModel.setDay(day);
            scheduleDataModel.setWeek_day(week_day);

            scheduleDataModels.add(scheduleDataModel);
        }
        return scheduleDataModels;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIterationListener) {
            mListener = (FragmentIterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action, Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action, bundle);
        }
    }
}
