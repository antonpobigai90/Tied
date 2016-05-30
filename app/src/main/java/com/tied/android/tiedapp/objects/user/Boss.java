package com.tied.android.tiedapp.objects.user;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by Emmanuel on 5/28/2016.
 */
public class Boss implements Serializable {
    public static final String TAG = Boss.class.getSimpleName();

    private String id;
    private String email;
    private String first_name;
    private String last_name;
    private String phone;

    public Location office_address;

}
