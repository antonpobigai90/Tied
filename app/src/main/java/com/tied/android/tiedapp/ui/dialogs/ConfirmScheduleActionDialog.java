package com.tied.android.tiedapp.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.responses.ScheduleRes;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.adapters.ScheduleListAdapter;
import com.tied.android.tiedapp.util.MyUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZuumaPC on 7/7/2016.
 */
public class ConfirmScheduleActionDialog {

    public static final String TAG = ConfirmScheduleActionDialog.class
            .getSimpleName();

    Activity activity;
    private TextView cancel, yes, txt_content, txt_heading;
    private Schedule schedule;
    Activity _c;
    Bundle bundle;
    private ScheduleListAdapter adapter;
    private int type;

    public ConfirmScheduleActionDialog(Schedule schedule1, ScheduleListAdapter adapter, Activity activity, Bundle bundle1, int type){
        this.activity = activity;

        schedule = schedule1;
        _c = activity;
        bundle = bundle1;
        this.adapter = adapter;
        this.type = type;
    }

    public void showDialog(){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_delete_schedule);

        txt_heading = (TextView) dialog.findViewById(R.id.txt_heading);
        txt_content = (TextView) dialog.findViewById(R.id.txt_content);

        String content = "";
        String heading = "";
        switch (type){
            case 1:
                heading = "Mark as Completed";
                content = "Are you sure you want to make the appointment as completed";
                break;
            case 2:
                heading = "Cancel Appointment";
                content = "Are you sure you cancel this appointment";
                break;
            case 3:
                heading = "Delete Appointment";
                content = "Are you sure you want to delete this appointment";
                break;
        }

        txt_heading.setText(heading);
        txt_content.setText(content);

        yes = (TextView) dialog.findViewById(R.id.yes);

        cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Schedule successfully deleted";
                switch (type){
                    case 1:
                        schedule.setStatus(1);
                        message = "Appointment marked as completed";
                        updateSchedule(schedule);
                        break;
                    case 2:
                        schedule.setStatus(2);
                        message = "Appointment canceled successfully";
                        updateSchedule(schedule);
                        break;
                    case 3:
                        deleteSchedule(schedule);
                        break;
                }

                dialog.dismiss();
                ((MainActivity) _c).ShowSuccessMessage(message, Constants.SCHEDULE_DELETED);
            }
        });

        dialog.show();

    }


    public void updateSchedule(final Schedule schedule) {
        Log.d(TAG + " schedule", schedule.toString());
        DialogUtils.displayProgress(_c);
        User user = MyUtils.getUserFromBundle(bundle);
        final ScheduleApi scheduleApi = MainApplication.getInstance().getRetrofit().create(ScheduleApi.class);
        Call<ScheduleRes> response = scheduleApi.updateSchedule(user.getToken(), schedule.getId(), schedule);
        response.enqueue(new Callback<ScheduleRes>() {
            @Override
            public void onResponse(Call<ScheduleRes> call, Response<ScheduleRes> scheduleResResponse) {
                if (_c == null) return;
                try {
                    ScheduleRes scheduleRes = scheduleResResponse.body();
                    if (scheduleRes != null && scheduleRes.isAuthFailed()) {
                        DialogUtils.closeProgress();
                        User.LogOut(_c);
                    } else if (scheduleRes != null && scheduleRes.get_meta() != null && scheduleRes.get_meta().getStatus_code() == 200) {
                        Log.d(TAG + " Schedule", scheduleRes.getSchedule().toString());
                        Schedule updatedSchedule = scheduleRes.getSchedule();
                        if (updatedSchedule.getId().equals(schedule.getId())) {
                            MyUtils.showToast(scheduleRes.get_meta().getUser_message());
                            adapter.updateStatus(schedule.getId(), schedule.getStatus());
                            DialogUtils.closeProgress();
                        }
                    } else {
                        //Toast.makeText(getActivity(), scheduleRes.toString(), Toast.LENGTH_LONG).show();
                        MyUtils.showAlert(_c, scheduleRes.getMessage());
                        DialogUtils.closeProgress();
                    }
                }catch (Exception e) {
                    MyUtils.showConnectionErrorToast(_c);
                }
            }
            @Override
            public void onFailure(Call<ScheduleRes> ScheduleResponseCall, Throwable t) {
                // Toast.makeText(getActivity(), "On failure : error encountered", Toast.LENGTH_LONG).show();
                MyUtils.showConnectionErrorToast(_c);
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }

    public void deleteSchedule(final Schedule schedule){
        Gson gson = new Gson();
        String user_json = bundle.getString(Constants.USER_DATA);
        User user = gson.fromJson(user_json, User.class);
        Log.d(TAG + "schedule", schedule.toString());

        ScheduleApi scheduleApi = MainApplication.getInstance().getRetrofit().create(ScheduleApi.class);
        Call<ResponseBody> response = scheduleApi.deleteSchedule(user.getToken(), schedule.getId());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> resResponse) {
                if (_c == null) return;
                Log.d(TAG + "ClientRes", resResponse.toString());
                DialogUtils.closeProgress();
                GeneralResponse generalResponse = new GeneralResponse(resResponse.body());
                try {
                    _Meta meta = generalResponse.getMeta();
                    if (meta.getStatus_code() == 200){
                        MyUtils.showToast(meta.getUser_message());
                        adapter.remove(schedule.getId());
                    }else{
                        MyUtils.showToast("Error encountered");
                    }
                } catch (Exception e) {
                    MyUtils.showToast(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
}
