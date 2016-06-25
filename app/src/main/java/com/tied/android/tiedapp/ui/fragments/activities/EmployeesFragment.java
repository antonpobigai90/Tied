package com.tied.android.tiedapp.ui.fragments.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tied.android.tiedapp.R;

/**
 * Created by Ratan on 7/29/2015.
 */
public class EmployeesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_create_appointment,null);
    }
}
