package com.tied.android.tiedapp.util;

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

        public void showDialog(Activity activity, String txt_heading, String txt_content, String txt_yes){
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

        cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
