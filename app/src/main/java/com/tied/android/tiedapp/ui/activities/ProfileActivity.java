package com.tied.android.tiedapp.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.fragments.profile.ProfileFragment;

public class ProfileActivity extends FragmentActivity {

    public static final String TAG = ProfileActivity.class
            .getSimpleName();

    User user;
    Bundle bundle;

    private Fragment fragment = null;
    private int fragment_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = User.getUser(getApplicationContext());
        bundle = new Bundle();
        Gson gson = new Gson();
        String user_json = gson.toJson(user);
        bundle.putString(Constants.USER, user_json);
        launchFragment(Constants.Profile, bundle);
    }


    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;

        switch (pos) {
            case Constants.Profile:
                fragment = new ProfileFragment();
                fragment.setArguments(bundle);
                break;
            default:
                finish();
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
