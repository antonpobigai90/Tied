package com.tied.android.tiedapp.ui.fragments.schedule.tabs;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.objects.responses.ScheduleRes;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.ui.adapters.ScheduleListAdapter;
import com.tied.android.tiedapp.util.DialogUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Emmanuel on 7/15/2016.
 */
public class AllScheduleFragment extends SchedulesFragment implements View.OnClickListener{

    public static final String TAG = AllScheduleFragment.class
            .getSimpleName();

    protected void initComponent(View view) {
        super.initComponent(view);
    }

    protected void initSchedule() {
        ScheduleApi scheduleApi = MainApplication.getInstance().getRetrofit().create(ScheduleApi.class);
        Call<ScheduleRes> response = scheduleApi.getSchedule(user.getToken());
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
                    scheduleDataModels = parseSchedules(scheduleArrayList);
                    adapter = new ScheduleListAdapter(scheduleDataModels, getActivity(), bundle);
                    listView.setAdapter(adapter);
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
}
