package com.tied.android.tiedapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.util.ScheduleDialog;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_line);

        ScheduleDialog alert = new ScheduleDialog();
        alert.showDialog(this);
    }
}
