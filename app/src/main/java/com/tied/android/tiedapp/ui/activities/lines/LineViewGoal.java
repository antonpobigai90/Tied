package com.tied.android.tiedapp.ui.activities.lines;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.util.MyUtils;

public class LineViewGoal extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LineViewGoal.class
            .getSimpleName();
    private User user;
    private Bundle bundle;

    LinearLayout back_layout;
    ImageView img_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_goal_view);

        user = MyUtils.getUserLoggedIn();

        back_layout = (LinearLayout) findViewById(R.id.back_layout);
        img_add = (ImageView) findViewById(R.id.img_add);
        if (img_add != null) {
            img_add.setOnClickListener(this);
        }
        if (back_layout != null) {
            back_layout.setOnClickListener(this);
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.img_add:
                MyUtils.startActivity(this, LineAddGoal.class);
                break;
        }
    }

}
