package com.tied.android.tiedapp.ui.activities.signups;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.interfaces.retrofits.SignUpApi;
import com.tied.android.tiedapp.ui.fragments.signins.DoneResetFragment;
import com.tied.android.tiedapp.ui.fragments.signins.ResetFragment;
import com.tied.android.tiedapp.ui.fragments.signins.SignInFragment;
import com.tied.android.tiedapp.ui.listeners.SignUpFragmentListener;

import retrofit2.Retrofit;

public class SignInActivity extends AppCompatActivity implements SignUpFragmentListener {

    public static final String TAG = SignInActivity.class
            .getSimpleName();

    private Fragment fragment = null;
    private int fragment_index = 0;

    public Bitmap bitmap;

    public Retrofit retrofit;
    public SignUpApi service;

    public Uri imageUri = null, outputUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        launchFragment(Constants.SignInUser, null);

        retrofit = MainApplication.getInstance().getRetrofit();
        service = retrofit.create(SignUpApi.class);

    }

    public void launchFragment(int pos, Bundle bundle) {
        fragment_index = pos;
        fragment = null;

        switch (pos) {
            case Constants.SignInUser:
                fragment = new SignInFragment();
                fragment.setArguments(bundle);
                break;
            case Constants.Reset:
                fragment = new ResetFragment();
                fragment.setArguments(bundle);
                break;
            case Constants.DoneReset:
                fragment = new DoneResetFragment();
                fragment.setArguments(bundle);
                break;
        }

        if (fragment != null) {
            Log.d(TAG, getSupportFragmentManager().getBackStackEntryCount() + "");
            while (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        Log. d(TAG, "fragment_index " + fragment_index);
        if (fragment_index == Constants.Reset) {
            launchFragment(Constants.SignInUser, null);
        } else {
            finish();
        }
    }

    @Override
    public void onFragmentInteraction(int action, Bundle bundle) {
        Log.d(TAG, " onFragmentInteraction "+action);
        launchFragment(action, bundle);
    }

}
