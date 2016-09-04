package com.tied.android.tiedapp.ui.activities.lines;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.DatePickerFragment;
import com.tied.android.tiedapp.util.MyUtils;

public class LineAddGoal extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LineAddGoal.class
            .getSimpleName();
    private User user;
    private Bundle bundle;

    LinearLayout back_layout, layout_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_add_goal);

        user = MyUtils.getUserLoggedIn();

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        layout_date = (LinearLayout) findViewById(R.id.layout_date);

        if (back_layout != null) {
            back_layout.setOnClickListener(this);
        }
        if (layout_date != null) {
            layout_date.setOnClickListener(this);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.layout_date:
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.show(this.getSupportFragmentManager(), "datePicker");
                break;

        }
    }

}
