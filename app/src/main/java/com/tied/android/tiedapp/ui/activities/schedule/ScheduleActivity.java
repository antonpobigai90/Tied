package com.tied.android.tiedapp.ui.activities.schedule;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.ProfileActivity;
import com.tied.android.tiedapp.ui.activities.signups.SignUpActivity;
import com.tied.android.tiedapp.ui.fragments.activities.EmployeesFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.AddActivityFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.CreateScheduleFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.HomeScheduleFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentInterationListener;


/**
 * Created by Daniel on 5/3/2016.
 */
public class ScheduleActivity extends FragmentActivity implements FragmentInterationListener, View.OnClickListener{

    public static final String TAG = SignUpActivity.class
            .getSimpleName();

    User user;

    public ImageView img_user_picture;

    private Fragment fragment = null;
    private int fragment_index = 0;

    private LinearLayout tab_bar,relativeLayout,selectedlayout, more;

    private TextView txt_schedules, txt_activities;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = (LinearLayout) findViewById(R.id.linearLayout);
        tab_bar = (LinearLayout) findViewById(R.id.tab_bar);

        more = (LinearLayout) findViewById(R.id.more);

        selectedlayout = (LinearLayout) findViewById(R.id.activity);
        selectedlayout.setBackground(getResources().getDrawable(R.drawable.tab_selected));

        txt_activities = (TextView) findViewById(R.id.txt_activities);
        txt_schedules = (TextView) findViewById(R.id.txt_schedules);

        more.setOnClickListener(this);
        txt_schedules.setOnClickListener(this);
        txt_activities.setOnClickListener(this);

        img_user_picture = (ImageView) findViewById(R.id.img_user_picture);
        user = User.getUser(getApplicationContext());
        if (user.getAvatar_uri() != null) {
            Uri myUri = Uri.parse(user.getAvatar_uri());
            img_user_picture.setImageURI(myUri);
        }else{
            Picasso.with(this).
                    load(Constants.GET_AVATAR_ENDPOINT+"avatar_"+user.getId()+".jpg")
                    .resize(35,35)
                    .into(img_user_picture);
        }
        bundle = new Bundle();
        Gson gson = new Gson();
        String user_json = gson.toJson(user);
        bundle.putString(Constants.USER, user_json);
        if(user.isNewUser(getApplicationContext())){
            launchFragment(Constants.HomeSchedule, bundle);
        }else{
            launchFragment(Constants.CreateSchedule, bundle);
        }
    }

    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;

        this.bundle = bundle;

        relativeLayout.setVisibility(View.VISIBLE);

        switch (pos) {
            case Constants.CreateSchedule:
                fragment = new CreateScheduleFragment();
                break;
            case Constants.ActivitySchedule:
                relativeLayout.setVisibility(View.GONE);
                fragment = new AddActivityFragment();
                break;
            case Constants.HomeSchedule:
                tab_bar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                fragment = new HomeScheduleFragment();
                break;
            case Constants.TabActivities:
                fragment = new EmployeesFragment();
                break;
            default:
                finish();
        }

        fragment.setArguments(bundle);
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

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {
        Log.d(TAG, " onFragmentInteraction " + action);
        launchFragment(action, bundle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_activities:
                launchFragment(Constants.TabActivities, bundle);
                break;
            case R.id.txt_schedules:
                launchFragment(Constants.CreateSchedule, bundle);
                break;
            case R.id.more:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
        }
    }
}
