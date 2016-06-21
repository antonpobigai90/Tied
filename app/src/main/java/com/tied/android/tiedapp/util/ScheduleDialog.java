package com.tied.android.tiedapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.tied.android.tiedapp.R;

/**
 * Created by Emmanuel on 6/21/2016.
 */
public class ScheduleDialog {

    RelativeLayout cancel;

//    public void showDialog(Activity activity, String msg){
    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.schedule_dialog);

//        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
//        text.setText(msg);

        cancel = (RelativeLayout) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
