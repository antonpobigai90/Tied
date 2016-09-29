package com.tied.android.tiedapp.ui.activities.goal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Goal;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.util.MyUtils;

public class LineViewGoalActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LineViewGoalActivity.class
            .getSimpleName();
    private User user;
    private Bundle bundle;
    private Goal goal;

    ImageView back_button, img_edit;
    TextView txt_title, goal_size, expire, description, lines_client_count;
    ProgressBar goal_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_goal_view);

        expire = (TextView) findViewById(R.id.expire);
        goal_progress = (ProgressBar) findViewById(R.id.progressBar);
        txt_title = (TextView) findViewById(R.id.txt_title);
        description = (TextView) findViewById(R.id.description);
        lines_client_count = (TextView) findViewById(R.id.lines_client_count);
        goal_size = (TextView) findViewById(R.id.goal_size);

        bundle = getIntent().getExtras();
//        goal = (Goal) bundle.getSerializable(Constants.GOAL_DATA);
        user = MyUtils.getUserFromBundle(bundle);

//        txt_title.setText(goal.getTitle());
//        description.setText(goal.getDescription());
//        goal_progress.setText(goal.getProgress());
//        expire.setText(goal.getExpirationDate());
//        lines_client_count.setText(goal.getClientLinesCountString());

        user = MyUtils.getUserLoggedIn();

        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(this);

        img_edit = (ImageView) findViewById(R.id.img_edit);
        img_edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.img_edit:
                MyUtils.startActivity(this, GoalEditActivity.class);
                break;
        }
    }

}
