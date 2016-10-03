package com.tied.android.tiedapp.ui.activities.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.listeners.FragmentIterationListener;
import com.tied.android.tiedapp.util.MyUtils;

/**
 * Created by hitendra on 9/9/2016.
 */
public class PrivacyActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlSales, rlDailyActivities, rlClients, rlTeritorry, rlLine, rlIndustry;
    ImageView img_close;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_privacy);

        bundle = getIntent().getExtras();
        init();
    }

    private void init() {
        rlSales = (RelativeLayout) findViewById(R.id.rlSales);
        rlSales.setOnClickListener(this);
        rlDailyActivities = (RelativeLayout) findViewById(R.id.rlDailyActivities);
        rlDailyActivities.setOnClickListener(this);
        rlClients = (RelativeLayout) findViewById(R.id.rlClients);
        rlClients.setOnClickListener(this);
        rlTeritorry = (RelativeLayout) findViewById(R.id.rlTeritorry);
        rlTeritorry.setOnClickListener(this);
        rlLine = (RelativeLayout) findViewById(R.id.rlLine);
        rlLine.setOnClickListener(this);
        rlIndustry = (RelativeLayout) findViewById(R.id.rlIndustry);
        rlIndustry.setOnClickListener(this);
        img_close = (ImageView) findViewById(R.id.img_close);
        img_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.rlSales:
                MyUtils.startActivity(this, SalePrivacyActivity.class, bundle);
                break;
            case R.id.rlDailyActivities:
                break;
            case R.id.rlClients:
                break;
            case R.id.rlTeritorry:
                break;
            case R.id.rlLine:
                break;
            case R.id.rlIndustry:
                MyUtils.startActivity(this, IndustryActivity.class, bundle);
                break;
        }
    }

}
