package com.tied.android.tiedapp.ui.activities.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.fragments.schedule.CreateScheduleFragment;


/**
 * Created by Daniel on 5/3/2016.
 */
public class ScheduleActivity extends FragmentActivity {

    public static final String TAG = SignUpActivity.class
            .getSimpleName();

    User user;

    LinearLayout logout;

    private Fragment fragment = null;
    private int fragment_index = 0;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        user = User.getUser(getApplicationContext());
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String user_json = gson.toJson(user);
        bundle.putString(Constants.USER, user_json);
        launchFragment(Constants.CreateSchedule, bundle);


        logout = (LinearLayout) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.LogOut(getApplicationContext());
            }
        });
    }

    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;

        this.bundle = bundle;

        switch (pos) {
            case Constants.CreateSchedule:
                fragment = new CreateScheduleFragment();
                fragment.setArguments(bundle);
                break;
            default: finish();
        }

        if (fragment != null) {
            Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            while (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_place, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }
    }
}
