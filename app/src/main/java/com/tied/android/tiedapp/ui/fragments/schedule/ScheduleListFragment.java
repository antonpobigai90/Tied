package com.tied.android.tiedapp.ui.fragments.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.tied.android.tiedapp.customs.db.DatabaseHelper;
import com.tied.android.tiedapp.customs.db.table.ScheduleTable;
import com.tied.android.tiedapp.customs.model.ScheduleDataModel;
import com.tied.android.tiedapp.customs.model.ScheduleTimeModel;
import com.tied.android.tiedapp.objects.Schedule;
import com.tied.android.tiedapp.objects.responses.ScheduleRes;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.ui.adapters.ScheduleListAdapter;
import com.tied.android.tiedapp.ui.listeners.FragmentInterationListener;
import com.tied.android.tiedapp.util.DialogUtils;

import java.util.ArrayList;
import java.util.List;

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

    public FragmentInterationListener mListener;


    private ArrayList<ScheduleDataModel> schedules;
    private ListView listView;

    private ScheduleListAdapter adapter;
    private Bundle bundle;
    private User user;

    String[] DAY = {"18","20","21"};
    String[] WEEK_DAY = {"Mon","Wed","Thu"};
    String[] TEMPERATURE = {"72°","40°","32°"};
    String[] WEATHER = {"Sunny","Cloudy","Normal"};
    ScheduleTimeModel[] scheduleTimeModel = {new ScheduleTimeModel("1","Birthday","All day"),new ScheduleTimeModel("2","Birthday Gurl","All day"),new ScheduleTimeModel("3","Birthday Boy","2pm")};

    ArrayList<ScheduleTimeModel> timeModel = new ArrayList<>();

    public static Fragment newInstance(int index){
        Bundle bundle2 = new Bundle();
        switch (index){

        }

        ScheduleListFragment scheduleListFragment = new ScheduleListFragment();
        scheduleListFragment.setArguments(bundle2);
        return  scheduleListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule_list,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    public void initComponent(View view) {
        schedules = new ArrayList<ScheduleDataModel>();
        listView = (ListView) view.findViewById(R.id.list);
        bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            String user_json = bundle.getString("user");
            user = gson.fromJson(user_json, User.class);
        }
//        initSchedule();
        ArrayList<ScheduleDataModel> scheduleDataModels = parseScheduleList();
        adapter = new ScheduleListAdapter(scheduleDataModels, getActivity());
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void initSchedule(){

        ScheduleApi scheduleApi =  MainApplication.getInstance().getRetrofit().create(ScheduleApi.class);
        Call<ScheduleRes> response = scheduleApi.getSchedule(user.getToken());
        response.enqueue(new Callback<ScheduleRes>() {
            @Override
            public void onResponse(Call<ScheduleRes> call, Response<ScheduleRes> resResponse) {
                if (getActivity() == null) return;
                Log.d(TAG + " here", resResponse.toString());
                DialogUtils.closeProgress();
                ScheduleRes scheduleRes = resResponse.body();
                if(scheduleRes.isAuthFailed()){
                    User.LogOut(getActivity());
                }
                else if(scheduleRes.get_meta() != null && scheduleRes.get_meta().getStatus_code() == 200){
                    ArrayList<Schedule> scheduleArrayList = scheduleRes.getSchedules();
                    for (Schedule schedule: scheduleArrayList){
                        storeScheduleToDb(schedule);
                    }

                    ArrayList<ScheduleDataModel> scheduleDataModels = parseScheduleList();
                    Log.d(TAG + " here", scheduleDataModels.toString());
                    adapter = new ScheduleListAdapter(scheduleDataModels, getActivity());
                    listView.setAdapter(adapter);
                }else{
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

    private ArrayList<ScheduleDataModel> parseScheduleList(){
        ArrayList<ScheduleDataModel> scheduleDataModelArrayList = new ArrayList<ScheduleDataModel>();

        ArrayList<ScheduleTimeModel> scheduleTimeModelArrayList = new ArrayList<>();
        for (Schedule schedule: retrieveSchedulesFromBD()){
            ScheduleDataModel scheduleDataModel = new ScheduleDataModel();

            ScheduleTimeModel scheduleTimeModel = new ScheduleTimeModel(schedule.getId(), schedule.getTitle(), schedule.getId());
            scheduleTimeModelArrayList.add(scheduleTimeModel);
            scheduleDataModel.setScheduleTimeModel(scheduleTimeModelArrayList);
            scheduleDataModelArrayList.add(scheduleDataModel);
        }
        return scheduleDataModelArrayList;
    }

    /*
 * Creating a todo
 */
    public long storeScheduleToDb(Schedule schedule) {
        DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ScheduleTable.KEY_TITLE, schedule.getTitle());
        values.put(ScheduleTable.KEY_USER_ID, schedule.getUser_id());
        values.put(ScheduleTable.KEY_CLIENT_ID, schedule.getClient_id());
        values.put(ScheduleTable.KEY_DATE, schedule.getDate());
        values.put(ScheduleTable.KEY_REMINDER, 1);
        values.put(ScheduleTable.KEY_VISITED, 0);
        values.put(ScheduleTable.KEY_START_TIME, schedule.getStart_time());
        values.put(ScheduleTable.KEY_END_TIME, schedule.getEnd_time());
        values.put(ScheduleTable.KEY_LAT, schedule.getLocation().getCoordinate().getLat());
        values.put(ScheduleTable.KEY_LON, schedule.getLocation().getCoordinate().getLon());

        Log.d(TAG + " ContentValues ", values.toString());

        long schedule_id = sqLiteDatabase.insert(ScheduleTable.TABLE_SCHEDULE, null, values);
        if(schedule_id != -1){
            Log.d(TAG, "inserted id = "+schedule_id);
        }
        return schedule_id;
    }


    /*
 * get single todo
 */
    public List<Schedule> retrieveSchedulesFromBD() {
        DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        List<Schedule> schedules = new ArrayList<Schedule>();
        String selectQuery = "SELECT  * FROM " + ScheduleTable.TABLE_SCHEDULE;

        Log.d(TAG, selectQuery);

        Cursor c = sqLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Schedule schedule = new Schedule();
                schedule.setId(c.getString((c.getColumnIndex(ScheduleTable.KEY_ID))));
                schedule.setUser_id(c.getInt((c.getColumnIndex(ScheduleTable.KEY_USER_ID))));
                schedule.setClient_id(c.getString((c.getColumnIndex(ScheduleTable.KEY_CLIENT_ID))));
                schedule.setTitle(c.getString((c.getColumnIndex(ScheduleTable.KEY_TITLE))));
                schedule.setDate(c.getString((c.getColumnIndex(ScheduleTable.KEY_DATE))));
                schedule.setEnd_time(c.getString((c.getColumnIndex(ScheduleTable.KEY_END_TIME))));
                schedule.setStart_time(c.getString((c.getColumnIndex(ScheduleTable.KEY_START_TIME))));
                schedule.setReminder((c.getInt(c.getColumnIndex(ScheduleTable.KEY_REMINDER))));
                schedule.setVisited(true);

                schedules.add(schedule);
            } while (c.moveToNext());
        }

        return schedules;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInterationListener) {
            mListener = (FragmentInterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action,Bundle bundle) {
        if (mListener != null) {
            mListener.OnFragmentInteractionListener(action, bundle);
        }
    }
}
