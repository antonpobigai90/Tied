package com.tied.android.tiedapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.ui.activities.schedule.CreateAppointmentActivity;

import retrofit2.Call;

/**
 * Created by Emmanuel on 7/18/2016.
 */
public class DialogScheduleEventOptions implements View.OnClickListener {

    public static final String TAG = DialogScheduleEventOptions.class
            .getSimpleName();
    RelativeLayout close;
    private TextView edit,delete,complete,cancel;
    private Dialog dialog;
    private Schedule schedule;
    Activity _c;
    Bundle bundle;

    public void showDialog(Schedule schedule1, Activity activity, Bundle bundle1){
        schedule = schedule1;
        _c = activity;
        bundle = bundle1;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);


        // Setting dialogview
        Window window = dialog.getWindow();

//        WindowManager.LayoutParams param = window.getAttributes();
//        param.gravity = Gravity.BOTTOM;
//        param.x = 100;   //x position left to right
//        param.y = 100;   //y position bottom to top
//        window.setAttributes(param);

//        window.setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.dialog_event_schedule_options);
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        edit = (TextView) dialog.findViewById(R.id.edit);
        edit.setOnClickListener(this);
        complete = (TextView) dialog.findViewById(R.id.complete);
        cancel = (TextView) dialog.findViewById(R.id.cancel);
        delete = (TextView) dialog.findViewById(R.id.delete);


        close = (RelativeLayout) dialog.findViewById(R.id.close);
        close.setOnClickListener(this);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:
                dialog.dismiss();
                break;
            case R.id.edit:
                editSchedule(schedule);
                break;
        }
    }

    public void editSchedule(final Schedule schedule){
        Gson gson = new Gson();
        String user_json = bundle.getString(Constants.USER_DATA);
        User user = gson.fromJson(user_json, User.class);

        Log.d(TAG + "schedule", schedule.toString());

        ClientApi clientApi = MainApplication.getInstance().getRetrofit().create(ClientApi.class);
        Call<ClientRes> response = clientApi.getClientWithId(user.getToken(), schedule.getClient_id());
        response.enqueue(new retrofit2.Callback<ClientRes>() {
            @Override
            public void onResponse(Call<ClientRes> call, retrofit2.Response<ClientRes> resResponse) {
                if (_c == null) return;
                Log.d(TAG + "ClientRes", resResponse.toString());
                DialogUtils.closeProgress();
                ClientRes ClientRes = resResponse.body();
                if (ClientRes != null && ClientRes.isAuthFailed()) {
                    User.LogOut(_c);
                } else if (ClientRes != null && ClientRes.get_meta() != null && ClientRes.get_meta().getStatus_code() == 200) {
                    Client client = ClientRes.getClient();
                    Log.d(TAG + "client", client.toString());
                    Gson gson = new Gson();
                    Intent intent = new Intent(_c, CreateAppointmentActivity.class);
                    intent.putExtra(Constants.CLIENT_DATA, client);
                    intent.putExtra(Constants.SCHEDULE_DATA, schedule);
                    _c.startActivity(intent);
                } else {
                    Toast.makeText(_c, "encountered error with server", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ClientRes> call, Throwable t) {
                Log.d(TAG + " onFailure", t.toString());
                DialogUtils.closeProgress();
            }
        });
    }
}
