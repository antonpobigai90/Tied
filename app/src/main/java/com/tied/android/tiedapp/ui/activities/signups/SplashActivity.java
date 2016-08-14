package com.tied.android.tiedapp.ui.activities.signups;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.user.User;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.util.MyUtils;


/**
 * Created by yuweichen on 16/4/30.
 */
public class SplashActivity extends Activity {

    public static final String TAG = SplashActivity.class
            .getSimpleName();

    Context context;
    protected boolean _active = true;
    protected int _splashTime = 3000;

    Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_splash);
        final SplashActivity sPlashScreen = this;

        context = this;

        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean done = mPrefs.getBoolean(Constants.SPLASH_SCREEN_DONE, false);
        if (done) {
            if (User.isUserLoggedIn(getApplicationContext())) {
                MyUtils.startActivity(this, MainActivity.class);
            } else {
                MyUtils.startActivity(sPlashScreen, WalkThroughActivity.class);
            }

        } else {
            final Thread splashTread = new Thread() {
                @Override
                public void run() {
                    try {
                        int waited = 0;
                        while (_active && waited < _splashTime) {
                            sleep(100);
                            if (_active) {
                                waited += 100;
                            }
                        }
                    } catch (final InterruptedException e) {
                        // do nothing
                    } finally {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                prefsEditor.putBoolean(Constants.SPLASH_SCREEN_DONE, true);
                                prefsEditor.apply();
                                MyUtils.startActivity(sPlashScreen, WalkThroughActivity.class);
                            }
                        });
                    }
                }
            };
            splashTread.start();
        }
    }

}
