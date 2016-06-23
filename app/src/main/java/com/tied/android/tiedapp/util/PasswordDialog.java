package com.tied.android.tiedapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.tied.android.tiedapp.R;

/**
 * Created by Emmanuel on 6/23/2016.
 */
public class PasswordDialog {

    TextView old_password, new_password, confirm_password, cancel;

    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_change_password);

        old_password = (TextView) dialog.findViewById(R.id.old_password);
        new_password = (TextView) dialog.findViewById(R.id.new_password);
        confirm_password = (TextView) dialog.findViewById(R.id.confirm_password);


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
