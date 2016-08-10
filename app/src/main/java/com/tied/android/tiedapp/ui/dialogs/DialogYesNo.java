package com.tied.android.tiedapp.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.tied.android.tiedapp.R;

/**
 * Created by ZuumaPC on 7/7/2016.
 */
public class DialogYesNo {
    private TextView heading, content, cancel, yes;

    Activity activity;
    String txt_heading;
    String txt_content;
    String txt_yes;
    int color, type;

    public DialogYesNo(Activity activity, String txt_heading, String txt_content, String txt_yes, int color, int type){
        this.activity = activity;
        this.color = color;
        this.type = type;
        this.txt_heading = txt_heading;
        this.txt_content = txt_content;
        this.txt_yes = txt_yes;
    }

    public void showDialog(){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_yes_no);

        heading = (TextView) dialog.findViewById(R.id.txt_heading);
        content = (TextView) dialog.findViewById(R.id.txt_content);
        yes = (TextView) dialog.findViewById(R.id.yes);

        heading.setText(txt_heading);
        content.setText(txt_content);
        yes.setText(txt_yes);
//        yes.setTextColor(activity.getResources().getColor(R.color.semi_transparent_black));
        yes.setTextColor(color);

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
                switch (type){
                    case 0:
                        dialog.dismiss();
                        break;
                    case 1:

                        break;
                }
            }
        });

        dialog.show();

    }
}
