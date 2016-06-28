package com.tied.android.tiedapp.ui.fragments.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tied.android.tiedapp.R;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.ui.listeners.FragmentInterationListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateScheduleFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = CreateScheduleFragment.class
            .getSimpleName();

    private Bundle bundle;

    private TextView txt_create_schedule;

    private FragmentInterationListener fragmentInterationListener;

    public CreateScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponent(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInterationListener) {
            fragmentInterationListener = (FragmentInterationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void nextAction(int action, Bundle bundle) {
        if (fragmentInterationListener != null) {
            fragmentInterationListener.OnFragmentInteractionListener(action,bundle);
        }
    }

    public void initComponent(View view) {
        txt_create_schedule = (TextView) view.findViewById(R.id.txt_create_schedule);
        txt_create_schedule.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_create_schedule:
                nextAction(Constants.ActivitySchedule, bundle);
                break;
        }
    }
}