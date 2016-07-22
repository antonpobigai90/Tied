package com.tied.android.tiedapp.util;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import  com.tied.android.tiedapp.util.States;

import java.util.ArrayList;
import java.util.List;

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
    public static class States {
       public static List<String> asArrayList() {
           JSONArray states = com.tied.android.tiedapp.util.States.asJSONArray();
           int len = states.length();
           List<String> codes = new ArrayList<String>(len);
           for (int i = 0; i < len; i++) {
               try {
                   codes.add(states.getJSONObject(i).getString("code"));
               } catch (JSONException je) {

               }
           }
           return codes;
       }
    }
}
