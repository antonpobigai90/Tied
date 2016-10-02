package com.tied.android.tiedapp.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.client.Client;
import com.tied.android.tiedapp.objects.responses.ClientRes;
import com.tied.android.tiedapp.objects.responses.GeneralResponse;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.retrofits.services.ClientApi;
import com.tied.android.tiedapp.retrofits.services.ScheduleApi;
import com.tied.android.tiedapp.ui.activities.schedule.CreateAppointmentActivity;
import com.tied.android.tiedapp.ui.activities.schedule.ViewSchedule;
import com.tied.android.tiedapp.ui.adapters.ScheduleListAdapter;
import com.tied.android.tiedapp.util.MyUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Emmanuel on 7/18/2016.
 */
public class DialogNewClient implements View.OnClickListener {

    public static final String TAG = DialogNewClient.class
            .getSimpleName();
    RelativeLayout close;
    private TextView edit,delete,mark_as_completed,cancel_appointment, view;
    private Dialog dialog;
    Activity _c;
    Bundle bundle;
    private ScheduleListAdapter adapter;

    public void showDialog(Activity activity, Bundle bundle1){
        _c = activity;
        bundle = bundle1;
        this.adapter = adapter;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        // Setting dialogview
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.dialog_add_client);
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        RelativeLayout schedule_layout = (RelativeLayout) dialog.findViewById(R.id.schedule_layout);
        schedule_layout.setOnClickListener(this);

        RelativeLayout sale_layout = (RelativeLayout) dialog.findViewById(R.id.sale_layout);
        sale_layout.setOnClickListener(this);

        RelativeLayout client_layout = (RelativeLayout) dialog.findViewById(R.id.client_layout);
        client_layout.setOnClickListener(this);

        RelativeLayout goal_layout = (RelativeLayout) dialog.findViewById(R.id.goal_layout);
        goal_layout.setOnClickListener(this);

        RelativeLayout line_layout = (RelativeLayout) dialog.findViewById(R.id.line_layout);
        line_layout.setOnClickListener(this);

        AppCompatTextView atvCancel = (AppCompatTextView) dialog.findViewById(R.id.atvCancel);
        atvCancel.setOnClickListener(this);

        dialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.atvCancel:
                dialog.dismiss();
                break;
            case R.id.schedule_layout:
                dialog.dismiss();
                break;
            case R.id.sale_layout:
                dialog.dismiss();
                break;
            case R.id.client_layout:
                MyUtils.showNewLineDialog(_c, "Add New Client", new MyUtils.MyDialogClickListener() {
                    @Override
                    public void onClick(Object response) {

                    }
                });
                dialog.dismiss();
                break;
            case R.id.goal_layout:
                dialog.dismiss();
                break;
            case R.id.line_layout:
                dialog.dismiss();
                break;

        }
    }

}
