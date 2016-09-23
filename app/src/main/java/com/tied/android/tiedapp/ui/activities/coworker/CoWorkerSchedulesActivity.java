package com.tied.android.tiedapp.ui.activities.coworker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ScheduleDataModel;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.ScheduleRes;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.ui.adapters.ScheduleListAdapter;
import com.tied.android.tiedapp.ui.dialogs.DialogUtils;
import com.tied.android.tiedapp.util.HelperMethods;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 9/9/2016.
 */
public class CoWorkerSchedulesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String TAG = CoWorkerActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;
    private Client client;

    protected ArrayList<ScheduleDataModel> scheduleDataModels;
    protected ListView listView;

    protected ScheduleListAdapter adapter;

    protected View emptyScheduleMessage;
    protected ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coworker_schedules);

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

        emptyScheduleMessage = findViewById(R.id.empty_schedule);
        emptyScheduleMessage.setVisibility(View.GONE);
        pb=(ProgressBar) findViewById(R.id.progress_bar);
        pb.setVisibility(View.GONE);

        bundle = getIntent().getExtras();
        client = (Client) bundle.getSerializable(Constants.CLIENT_DATA);
        user = MyUtils.getUserFromBundle(bundle);
        initSchedule();
    }

    protected void initSchedule() {
        ScheduleApi scheduleApi = MainApplication.getInstance().getRetrofit().create(ScheduleApi.class);
        Call<ScheduleRes> response = scheduleApi.getSchedule(user.getToken());
        response.enqueue(new Callback<ScheduleRes>() {
            @Override
            public void onResponse(Call<ScheduleRes> call, Response<ScheduleRes> resResponse) {
                if (this == null) return;
                Log.d(TAG + "ScheduleRes", resResponse.toString());
                DialogUtils.closeProgress();
                ScheduleRes scheduleRes = resResponse.body();
                if (scheduleRes != null && scheduleRes.isAuthFailed()) {
                    User.LogOut(CoWorkerSchedulesActivity.this);
                } else if (scheduleRes != null && scheduleRes.get_meta() != null && scheduleRes.get_meta().getStatus_code() == 200) {
                    ArrayList<Schedule> scheduleArrayList = scheduleRes.getSchedules();
                    scheduleDataModels = parseSchedules(scheduleArrayList);
                    adapter = new ScheduleListAdapter(scheduleDataModels, CoWorkerSchedulesActivity.this, bundle);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(CoWorkerSchedulesActivity.this, "encountered error with server", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ScheduleRes> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    protected ArrayList<ScheduleDataModel> parseSchedules(ArrayList<Schedule> scheduleArrayList) {
        Log.d(TAG + " parseSchedules", scheduleArrayList.toString());
        ArrayList<ScheduleDataModel> scheduleDataModels = new ArrayList<>();
        for (int i = 0; i < scheduleArrayList.size(); i++) {
            Schedule schedule = scheduleArrayList.get(i);
            if (schedule.getClient_id().equals(client.getId())){
                ScheduleDataModel scheduleDataModel = new ScheduleDataModel();
                ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                schedules.add(schedule);
                for (int j = i + 1; j < scheduleArrayList.size(); j++) {
                    Schedule this_schedule = scheduleArrayList.get(j);
                    if (MyUtils.isSameDay(schedule.getDate(), this_schedule.getDate())) {
                        schedules.add(this_schedule);
                        Log.d(TAG, "SAME "+schedule.getTitle() + " and "+this_schedule.getTitle());
                        scheduleArrayList.remove(j--);
                    }
                }

                long diff_in_date = HelperMethods.getDateDifferenceWithToday(schedule.getDate());

                String day = String.format("%02d", HelperMethods.getDayFromSchedule(schedule.getDate()));
                String week_day = MyUtils.getWeekDay(schedule);

                scheduleDataModel.setSchedules(schedules);
                scheduleDataModel.setDay(day);
                scheduleDataModel.setWeek_day(week_day);

                scheduleDataModels.add(scheduleDataModel);
            }
        }
        Collections.reverse(scheduleDataModels);
        return scheduleDataModels;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
