package com.tied.android.tiedapp.ui.fragments.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ScheduleDataModel;
import com.tied.android.tiedapp.customs.model.ScheduleTimeModel;
import com.tied.android.tiedapp.objects.responses.ScheduleRes;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.schedule.ScheduleDate;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.ui.adapters.ScheduleListAdapter;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.DialogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ScheduleListFragment extends Fragment
        implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = ScheduleListFragment.class
            .getSimpleName();

    String[] MONTHS_LIST = {"January", "Febebuary", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};
    String[] WEEK_LIST = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};


    public FragmentIterationListener mListener;

    private ArrayList<ScheduleDataModel> schedules;
    private ListView listView;

    private ScheduleListAdapter adapter;
    private Bundle bundle;
    private User user;

    public static Fragment newInstance() {
        ScheduleListFragment scheduleListFragment = new ScheduleListFragment();
        return scheduleListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule_list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "Fragment changed");
        initComponent(view);
    }

    public void initComponent(View view) {
        schedules = new ArrayList<ScheduleDataModel>();
        adapter = new ScheduleListAdapter(schedules, getActivity());
        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);
        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString(Constants.USER_DATA);
            user = gson.fromJson(user_json, User.class);

            int index = bundle.getInt(Constants.SCHEDULE_DATA_FILTER_INDEX);
            String scheduleDate_json = bundle.getString(Constants.SCHEDULE_DATE_FILTER);
            ScheduleDate scheduleDate = gson.fromJson(scheduleDate_json, ScheduleDate.class);
            initSchedule(scheduleDate, index);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void initSchedule(ScheduleDate scheduleDate, final int index) {
        ScheduleApi scheduleApi = MainApplication.getInstance().getRetrofit().create(ScheduleApi.class);
        Call<ScheduleRes> response = scheduleApi.getScheduleByDate(user.getToken(), scheduleDate);
        response.enqueue(new Callback<ScheduleRes>() {
            @Override
            public void onResponse(Call<ScheduleRes> call, Response<ScheduleRes> resResponse) {
                if (getActivity() == null) return;
                Log.d(TAG + "ScheduleRes", resResponse.toString());
                DialogUtils.closeProgress();
                ScheduleRes scheduleRes = resResponse.body();
                if (scheduleRes.isAuthFailed()) {
                    User.LogOut(getActivity());
                } else if (scheduleRes.get_meta() != null && scheduleRes.get_meta().getStatus_code() == 200) {
                    ArrayList<Schedule> scheduleArrayList = scheduleRes.getSchedules();
                    Log.d(TAG, "index : "+index +" size "+scheduleArrayList.size());
                    schedules.clear();
                    if(index == 2){
                        parseDailySchedule(scheduleArrayList);
                    }else{
                        parseSchedules(scheduleArrayList);
                    }
                    Log.d(TAG + " scheduleArrayList", scheduleArrayList.toString());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), scheduleRes.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ScheduleRes> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    public void parseDailySchedule(ArrayList<Schedule> scheduleArrayList) {
        for (int i = 0; i < scheduleArrayList.size(); i++) {
            Schedule schedule = scheduleArrayList.get(i);

            ScheduleDataModel scheduleDataModel = new ScheduleDataModel();

            ScheduleTimeModel scheduleTimeModel = new ScheduleTimeModel(schedule.getId(),
                    schedule.getTitle(), schedule.getTime_range().getStart_time());

            ArrayList<ScheduleTimeModel> scheduleTimeModels = new ArrayList<ScheduleTimeModel>();
            scheduleTimeModels.add(scheduleTimeModel);

            String day = String.format("%02d", getDayFromSchedule(schedule.getDate()));
            String week_day = WEEK_LIST[getDayOfTheWeek(schedule.getDate()) - 1];

            scheduleDataModel.setScheduleTimeModel(scheduleTimeModels);
            scheduleDataModel.setTemperature("80");
            scheduleDataModel.setWeather("cloudy");
            scheduleDataModel.setDay(day);
            scheduleDataModel.setWeek_day(week_day);

            schedules.add(scheduleDataModel);
        }
    }

    public void parseSchedules(ArrayList<Schedule> scheduleArrayList) {
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

            String day = String.format("%02d", getDayFromSchedule(schedule.getDate()));
            String week_day = WEEK_LIST[getDayOfTheWeek(schedule.getDate()) - 1];

            scheduleDataModel.setScheduleTimeModel(scheduleTimeModels);
            scheduleDataModel.setTemperature("80");
            scheduleDataModel.setWeather("cloudy");
            scheduleDataModel.setDay(day);
            scheduleDataModel.setWeek_day(week_day);

            schedules.add(scheduleDataModel);
        }
    }

    public int getDayOfTheWeek(String day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(day);
            Calendar c = Calendar.getInstance();
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            c.setTime(date);
            return dayOfWeek;
        } catch (ParseException e) {
            return 0;
        }
    }

    private int getDayFromSchedule(String day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(day);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.DAY_OF_MONTH);
            return month;
        } catch (ParseException e) {
            return 0;
        }
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
