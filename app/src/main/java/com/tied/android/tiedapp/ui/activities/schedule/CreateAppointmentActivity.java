package com.tied.android.tiedapp.ui.activities.schedule;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.DatePickerFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.AppointmentCalendarFragment;
import com.tied.android.tiedapp.ui.fragments.schedule.CreateAppointmentFragment;
import com.tied.android.tiedapp.ui.listeners.FragmentInterationListener;


/**
 * Created by Daniel on 5/3/2016.
 */
public class CreateAppointmentActivity extends FragmentActivity implements FragmentInterationListener{
    public static final String TAG = CreateAppointmentFragment.class
            .getSimpleName();

    private Fragment fragment = null;
    public Fragment profileFragment = null;
    private int fragment_index = 0;

    User user;
    public Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        user = User.getUser(getApplicationContext());
        bundle = new Bundle();
        Gson gson = new Gson();
        String user_json = gson.toJson(user);
        bundle.putString(Constants.USER, user_json);
        launchFragment(Constants.CreateAppointment, bundle);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void OnFragmentInteractionListener(int action, Bundle bundle) {
        Log.d(TAG, " onFragmentInteraction " + action);
        launchFragment(action, bundle);
    }

    @Override
    public void onBackPressed() {
        if(fragment_index == Constants.CreateAppointment){
            finish();
        }
    }

    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;

        switch (pos) {
            case Constants.CreateAppointment:
                fragment = new CreateAppointmentFragment();
                break;
            case Constants.Appointment:
                fragment = new AppointmentCalendarFragment();
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

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit)
            ft.replace(R.id.fragment_place, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }
    }
}
