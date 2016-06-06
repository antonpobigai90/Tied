package com.tied.android.tiedapp.ui.activities.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tied.android.tiedapp.R;


/**
 * Created by Daniel on 5/3/2016.
 */
public class CreateApointmentActivity extends Activity implements View.OnClickListener{

    TextView txt_cancel, txt_title, txt_description, txt_date, txt_time, txt_location, txt_reminder;
    ImageView img_avatar, img_plus, img_plus1, img_location, img_reminder;
    TextView txt_create_schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        txt_cancel = (TextView) findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(this);

        txt_create_schedule = (TextView) findViewById(R.id.txt_create_schedule);
        txt_create_schedule.setOnClickListener(this);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_description = (TextView) findViewById(R.id.txt_description);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_location = (TextView) findViewById(R.id.txt_location);
        txt_reminder = (TextView) findViewById(R.id.txt_reminder);

        img_avatar = (ImageView) findViewById(R.id.img_avatar_schedule);

        img_plus = (ImageView) findViewById(R.id.img_plus);
        img_plus.setOnClickListener(this);

        img_plus1 = (ImageView) findViewById(R.id.img_plus1);
        img_plus1.setOnClickListener(this);

        img_location = (ImageView) findViewById(R.id.img_location);
        img_location.setOnClickListener(this);

        img_reminder = (ImageView) findViewById(R.id.img_reminder);
        img_reminder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.txt_cancel:
                finish();
                break;
            case R.id.img_plus:

                break;
            case R.id.img_plus1:

                break;
            case R.id.img_location:

                break;
            case R.id.img_reminder:

                break;
            case R.id.txt_create_schedule:

                break;
        }
    }
}
