package com.tied.android.tiedapp.ui.activities.coworker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.util.MyUtils;

/**
 * Created by Emmanuel on 9/9/2016.
 */
public class CoWorkerSchedulesActivity extends AppCompatActivity{

    public static final String TAG = CoWorkerActivity.class
            .getSimpleName();

    private Bundle bundle;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coworker_schedules);

        bundle = getIntent().getExtras();
        user = MyUtils.getUserFromBundle(bundle);
    }
}
