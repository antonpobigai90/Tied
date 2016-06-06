package com.tied.android.tiedapp.ui.activities.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tied.android.tiedapp.R;

/**
 * Created by Daniel on 5/3/2016.
 */
public class AddActivity extends Activity implements View.OnClickListener{

    TextView txt_done;
    RelativeLayout sale_layout, schedule_layout, client_layout, line_layout, employee_layout, goal_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_add_activity);

        txt_done = (TextView) findViewById(R.id.txt_done);
        txt_done.setOnClickListener(this);

        sale_layout = (RelativeLayout) findViewById(R.id.sale_layout);
        sale_layout.setOnClickListener(this);

        schedule_layout = (RelativeLayout) findViewById(R.id.schedule_layout);
        schedule_layout.setOnClickListener(this);

        client_layout = (RelativeLayout) findViewById(R.id.client_layout);
        client_layout.setOnClickListener(this);

        line_layout = (RelativeLayout) findViewById(R.id.line_layout);
        line_layout.setOnClickListener(this);

        employee_layout = (RelativeLayout) findViewById(R.id.employee_layout);
        employee_layout.setOnClickListener(this);

        goal_layout = (RelativeLayout) findViewById(R.id.goal_layout);
        goal_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.txt_done:
                finish();
                break;
            case R.id.sale_layout:

                break;
            case R.id.schedule_layout:

                break;
            case R.id.client_layout:

                break;
            case R.id.employee_layout:

                break;
            case R.id.line_layout:

                break;
            case R.id.goal_layout:

                break;
        }
    }
}
