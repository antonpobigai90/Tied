package com.tied.android.tiedapp.util;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;

/**
 * Created by Femi on 7/19/2016.
 */
public abstract class MyUtils {
    public static  class Picasso  {
        public  void displayImage( final String imageUrl, final ImageView imageView) {
            com.squareup.picasso.Picasso.with(MainApplication.getInstance().getApplicationContext())
                    .load(imageUrl)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            com.squareup.picasso.Picasso.with(MainApplication.getInstance().getApplicationContext())
                                    .load(imageUrl)
                                    .error(R.mipmap.ic_launcher)
                                    .into(imageView, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Logger.write("Picasso: Could not fetch image");
                                        }
                                    });
                        }
                    });
        }
    }
}
