package com.tied.android.tiedapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.client.Client;

/**
 * Created by Emmanuel on 7/20/2016.
 */
public class DialogClientOptions implements View.OnClickListener {

    public static final String TAG = DialogClientOptions.class
            .getSimpleName();
    RelativeLayout cancel;
    private TextView create_schedule,add_new_goal,add_new_line,add_new_sales;
    private Dialog dialog;
    private Client client;
    Activity activity;
    Bundle bundle;

    public DialogClientOptions(Client client, Activity activity, Bundle bundle){
        this.client = client;
        this.activity = activity;
        this.bundle = bundle;
    }

    public void showDialog(){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        // Setting dialogview
        Window window = dialog.getWindow();
        dialog.setContentView(R.layout.dialog_client_options);
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        create_schedule = (TextView) dialog.findViewById(R.id.create_schedule);
        add_new_line = (TextView) dialog.findViewById(R.id.add_new_line);
        add_new_goal = (TextView) dialog.findViewById(R.id.add_new_goal);
        add_new_sales = (TextView) dialog.findViewById(R.id.add_new_sales);

        create_schedule.setOnClickListener(this);
        add_new_line.setOnClickListener(this);
        add_new_sales.setOnClickListener(this);
        add_new_goal.setOnClickListener(this);


        cancel = (RelativeLayout) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                dialog.dismiss();
                break;
            case R.id.create_schedule:

                break;
            case R.id.add_new_goal:

                break;
            case R.id.add_new_sales:

                break;
            case R.id.add_new_line:

                break;

        }
    }

}
