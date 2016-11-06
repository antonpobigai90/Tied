package com.tied.android.tiedapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.client.AddClientActivity;
import com.tied.android.tiedapp.ui.activities.lines.AddLinesActivity;
import com.tied.android.tiedapp.ui.activities.schedule.CreateAppointmentActivity;
import com.tied.android.tiedapp.util.MyUtils;

/**
 * Created by Femi on 7/31/2016.
 */
public class SubscriptionActivity extends AppCompatActivity implements  View.OnClickListener {
    AppCompatActivity view;
    Bundle bundle;
    User user;
    ImageView img_month, img_year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        view=this;
        initComponent();
    }
    public void initComponent(){

        bundle = getIntent().getExtras();
        if (bundle != null) {
           user= MyUtils.getUserFromBundle(bundle);
        }

        img_month = (ImageView) findViewById(R.id.img_month);
        img_month.setOnClickListener(this);

        img_year = (ImageView) findViewById(R.id.img_year);
        img_year.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_month:
                break;
            case R.id.img_year:
                break;
        }
    }
    public void goBack(View view) {
        onBackPressed();
    }
}
