package com.tied.android.tiedapp.ui.fragments.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tied.android.tiedapp.R;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ScheduleListFragment extends Fragment {

    public static Fragment newInstance(int index){
        Bundle bundle2 = new Bundle();
        switch (index){

        }

        ScheduleTimeLineFragment scheduleTimeLineFragment = new ScheduleTimeLineFragment();
        scheduleTimeLineFragment.setArguments(bundle2);
        return  scheduleTimeLineFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule_list,null);
    }
}
