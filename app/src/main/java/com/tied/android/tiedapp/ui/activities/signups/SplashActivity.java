package com.tied.android.tiedapp.ui.activities.signups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;


/**
 * Created by yuweichen on 16/4/30.
 */
public class SplashActivity extends Activity {

    Context context;
    protected boolean _active = true;
    protected int _splashTime = 3000;

    Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final SplashActivity sPlashScreen = this;

        context = this;

        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean done = mPrefs.getBoolean(Constants.SPLASH_SCREEN_DONE, false);
        if (done) {
            Intent intent = new Intent(sPlashScreen, WalkThroughActivity.class);
            startActivity(intent);
            finish();
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
                                Intent intent = new Intent(sPlashScreen, WalkThroughActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            };

            splashTread.start();
        }
    }

}
